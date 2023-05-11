package com.flipperplz.bisutils.param.node.impl

import com.flipperplz.bisutils.param.node.RapElement
import com.flipperplz.bisutils.param.node.RapFile
import com.flipperplz.bisutils.param.node.RapStatement
import com.flipperplz.bisutils.param.statement.impl.RapClassImpl
import com.flipperplz.bisutils.param.utils.extensions.childrenOfType
import com.flipperplz.bisutils.utils.getAsciiZ
import com.flipperplz.bisutils.utils.getCompactInt
import com.flipperplz.bisutils.utils.getInt
import java.io.File
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel
import java.nio.file.StandardOpenOption

//TODO(Ryann): Change all to List and make lateinit. Rap should have mininal mutability
class RapFileImpl internal constructor(
    override val fileName: String
) : RapFile {
    override val slimParent: RapElement? = null
    override lateinit var slimEnum: Map<String, Int>
    override lateinit var slimCommands: List<RapStatement>

    companion object {

        operator fun invoke(file: File): RapFileImpl? = invoke(
            FileChannel.open(file.toPath(), StandardOpenOption.READ).use {
                ByteBuffer.allocate(it.size().toInt()).apply {
                    it.read(this)
                    flip()
                }
            },
            file.nameWithoutExtension
        )
        operator fun invoke(buffer: ByteBuffer, name: String): RapFileImpl? {
            if (
                buffer.get() != 0.toByte() ||
                buffer.get() != 114.toByte() ||
                buffer.get() != 97.toByte() ||
                buffer.get() != 80.toByte() ||
                buffer.getInt(ByteOrder.LITTLE_ENDIAN) != 0 ||
                buffer.getInt(ByteOrder.LITTLE_ENDIAN) != 8
            ) return null
            fun loadChildClasses(child: RapClassImpl, buffer: ByteBuffer): Boolean {
                with(mutableListOf<RapStatement>()) {
                    buffer.position(child.binaryOffset)
                    child.slimSuperClass = buffer.getAsciiZ()

                    for (i in 0 until buffer.getCompactInt())
                        add(RapStatement(buffer, child) ?: return false)

                    child.slimCommands = this
                }

                return child.childrenOfType<RapClassImpl>().all { loadChildClasses(it, buffer) }
            }

            val enumOffset = buffer.getInt(ByteOrder.LITTLE_ENDIAN)
            val file = RapFileImpl(name)
            file.slimEnum = mutableMapOf()

            with(mutableListOf<RapStatement>()) {
                buffer.getAsciiZ()
                for (i in 0 until buffer.getCompactInt())
                    add(RapStatement(buffer, file) ?: return null)

                file.slimCommands = this
            }

            if (!file.childrenOfType<RapClassImpl>().all { loadChildClasses(it, buffer) }) return null


            buffer.position(enumOffset) //TODO: ENUMS

            return file
        }
    }
}