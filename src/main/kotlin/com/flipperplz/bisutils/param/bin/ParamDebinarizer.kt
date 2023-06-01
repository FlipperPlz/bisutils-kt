package com.flipperplz.bisutils.param.bin

import com.flipperplz.bisutils.param.ParamFile
import com.flipperplz.bisutils.param.ast.literal.ParamArray
import com.flipperplz.bisutils.param.ast.literal.ParamFloat
import com.flipperplz.bisutils.param.ast.literal.ParamInt
import com.flipperplz.bisutils.param.ast.literal.ParamString
import com.flipperplz.bisutils.param.ast.node.ParamElement
import com.flipperplz.bisutils.param.ast.node.ParamLiteralBase
import com.flipperplz.bisutils.param.ast.node.ParamStatement
import com.flipperplz.bisutils.param.ast.statement.ParamDeleteStatement
import com.flipperplz.bisutils.param.ast.statement.ParamExternalClass
import com.flipperplz.bisutils.param.utils.ParamOperatorTypes
import com.flipperplz.bisutils.param.utils.extensions.*
import com.flipperplz.bisutils.param.utils.extensions.RapClassImpl
import com.flipperplz.bisutils.param.utils.extensions.RapFileImpl
import com.flipperplz.bisutils.param.utils.extensions.RapVariableImpl
import com.flipperplz.bisutils.io.getAsciiZ
import com.flipperplz.bisutils.io.getCompactInt
import com.flipperplz.bisutils.io.getFloat
import com.flipperplz.bisutils.io.getInt
import java.nio.ByteBuffer
import java.nio.ByteOrder

object ParamDebinarizer {
    fun readString(parent: ParamElement?, buffer: ByteBuffer): ParamString =
            ParamString(parent, buffer.getAsciiZ())

    fun readFloat(parent: ParamElement?, buffer: ByteBuffer): ParamFloat =
            ParamFloat(parent, buffer.getFloat(ByteOrder.LITTLE_ENDIAN))

    fun readInt(parent: ParamElement?, buffer: ByteBuffer): ParamInt =
            ParamInt(parent, buffer.getInt(ByteOrder.LITTLE_ENDIAN))

    fun readExternalClass(parent: ParamElement?, buffer: ByteBuffer): ParamExternalClass =
            ParamExternalClass(parent, buffer.getAsciiZ())

    fun readDeleteStatement(parent: ParamElement?, buffer: ByteBuffer, locateTarget: ((ParamDeleteStatement) -> ParamExternalClass)? = null): ParamDeleteStatement =
            ParamDeleteStatement(parent, buffer.getAsciiZ(), locateTarget)

    fun readArray(parent: ParamElement?, buffer: ByteBuffer): ParamArray = ParamArray(parent, mutableListOf<ParamLiteralBase>().apply {
        repeat(buffer.getCompactInt()) {
            add(readLiteral(parent, buffer))
        }
    })

    fun readParamFile(name: String, buffer: ByteBuffer): ParamFile {

        val file = RapFileImpl(name)
        fun loadChildClasses(child: RapClassImpl, buffer: ByteBuffer): Boolean {
            with(mutableListOf<ParamStatement>()) {
                buffer.position(child.binaryOffset)
                child.slimSuperClass = buffer.getAsciiZ()

                repeat(buffer.getCompactInt()) { add(readStatement(child, buffer)) }

                child.slimCommands = this
            }

            return child.childrenOfType<RapClassImpl>().all { loadChildClasses(it, buffer) }
        }

        if(buffer.get() != 0.toByte() || buffer.get() != 114.toByte() || buffer.get() != 97.toByte() || buffer.get() != 80.toByte() ||
                buffer.getInt(ByteOrder.LITTLE_ENDIAN) != 0 || buffer.getInt(ByteOrder.LITTLE_ENDIAN) != 8) throw Exception("Read Failed")

        val enumOffset = buffer.getInt(ByteOrder.LITTLE_ENDIAN)

        with(mutableListOf<ParamStatement>()) {
            buffer.getAsciiZ()
            for (i in 0 until buffer.getCompactInt())
                add(readStatement(file, buffer))

            file.slimCommands = this
        }

        if(!file.childrenOfType<RapClassImpl>().all { loadChildClasses(it, buffer) }) throw Exception()


        buffer.position(enumOffset) //TODO: ENUMS

        return file
    }



    fun readStatement(parent: ParamElement?, buffer: ByteBuffer): ParamStatement = when(buffer.get()) {
        0.toByte() -> RapClassImpl(parent, buffer.getAsciiZ(), buffer.getInt(ByteOrder.LITTLE_ENDIAN))

        1.toByte() -> RapVariableImpl(parent, ParamOperatorTypes.ASSIGN, buffer.getAsciiZ()).apply {
            slimValue = readLiteral(id = buffer.get().toInt(), parent = this@apply, buffer = buffer)
        }

        2.toByte() -> RapVariableImpl(parent, ParamOperatorTypes.ASSIGN, buffer.getAsciiZ()).apply {
            slimValue = readLiteral(this, buffer)
        }

        3.toByte() -> readExternalClass(parent, buffer)

        4.toByte() -> readDeleteStatement(parent, buffer)

        5.toByte() -> RapVariableImpl(parent, ParamOperatorTypes.forFlag(buffer.get()), buffer.getAsciiZ()).apply {
            slimValue = readArray(parent, buffer)
        }
        else -> throw Exception()
    }

    fun readLiteral(parent: ParamElement?, id: Int, buffer: ByteBuffer): ParamLiteralBase = when (id) {
        0 -> readString(parent, buffer)
        1 -> readFloat(parent, buffer)
        2 -> readInt(parent, buffer)
        3 -> readArray(parent, buffer)
        else -> throw Exception()
    }

    fun readLiteral(parent: ParamElement?, buffer: ByteBuffer): ParamLiteralBase = readLiteral(parent, buffer.getInt(), buffer)
}