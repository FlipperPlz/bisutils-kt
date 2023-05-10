package com.flipperplz.bisutils.param.rap

import com.flipperplz.bisutils.param.rap.node.RapStatement
import com.flipperplz.bisutils.param.rap.statement.RapClass
import com.flipperplz.bisutils.param.rap.statement.impl.RapClassImpl
import com.flipperplz.bisutils.param.slim.node.ParamSlimCommand
import com.flipperplz.bisutils.param.slim.util.childrenOfType
import com.flipperplz.bisutils.utils.getAsciiZ
import com.flipperplz.bisutils.utils.getCompactInt
import com.flipperplz.bisutils.utils.getInt
import java.nio.ByteBuffer
import java.nio.ByteOrder


//TODO(Ryann): Change all to List and make lateinit. Rap should have mininal mutability
class RapFileImpl internal constructor(
    override val fileName: String
) : RapFile {
    override lateinit var slimEnum: Map<String, Int>
    override lateinit var slimCommands: List<RapStatement>

    companion object {
        operator fun invoke(buffer: ByteBuffer, name: String): RapFileImpl? {
            if(
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

            with(mutableListOf<RapStatement>()) {
                buffer.getAsciiZ()
                for (i in 0 until buffer.getCompactInt())
                    add(RapStatement(buffer, file) ?: return null)

                file.slimCommands = this
            }

            if(!file.childrenOfType<RapClassImpl>().all { loadChildClasses(it, buffer) }) return null


            buffer.position(enumOffset) //TODO: ENUMS

            return file
        }
    }
}