package com.flipperplz.bisutils.param.bin

import com.flipperplz.bisutils.param.ast.IParamFile
import com.flipperplz.bisutils.param.ast.literal.IParamArray
import com.flipperplz.bisutils.param.ast.literal.IParamFloat
import com.flipperplz.bisutils.param.ast.literal.IParamInt
import com.flipperplz.bisutils.param.ast.literal.IParamString
import com.flipperplz.bisutils.param.ast.node.IParamElement
import com.flipperplz.bisutils.param.ast.node.IParamStatement
import com.flipperplz.bisutils.param.ast.statement.IParamDeleteStatement
import com.flipperplz.bisutils.param.ast.statement.IParamExternalClass
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
    fun readString(parent: IParamElement?, buffer: ByteBuffer): IParamString =
            IParamString(parent, buffer.getAsciiZ())

    fun readFloat(parent: IParamElement?, buffer: ByteBuffer): IParamFloat =
            IParamFloat(parent, buffer.getFloat(ByteOrder.LITTLE_ENDIAN))

    fun readInt(parent: IParamElement?, buffer: ByteBuffer): IParamInt =
            IParamInt(parent, buffer.getInt(ByteOrder.LITTLE_ENDIAN))

    fun readExternalClass(parent: IParamElement?, buffer: ByteBuffer): IParamExternalClass =
            IParamExternalClass(parent, buffer.getAsciiZ())

    fun readDeleteStatement(parent: IParamElement?, buffer: ByteBuffer, locateTarget: ((IParamDeleteStatement) -> IParamExternalClass)? = null): IParamDeleteStatement =
            IParamDeleteStatement(parent, buffer.getAsciiZ(), locateTarget)

    fun readArray(parent: IParamElement?, buffer: ByteBuffer): IParamArray = IParamArray(parent, mutableListOf<IParamLiteralBase>().apply {
        repeat(buffer.getCompactInt()) {
            add(readLiteral(parent, buffer))
        }
    })

    fun readParamFile(name: String, buffer: ByteBuffer): IParamFile {

        val file = RapFileImpl(name)
        fun loadChildClasses(child: RapClassImpl, buffer: ByteBuffer): Boolean {
            with(mutableListOf<IParamStatement>()) {
                buffer.position(child.binaryOffset)
                child.paramSuperClassname = buffer.getAsciiZ()

                repeat(buffer.getCompactInt()) { add(readStatement(child, buffer)) }

                child.slimCommands = this
            }

            return child.childrenOfType<RapClassImpl>().all { loadChildClasses(it, buffer) }
        }

        if(buffer.get() != 0.toByte() || buffer.get() != 114.toByte() || buffer.get() != 97.toByte() || buffer.get() != 80.toByte() ||
                buffer.getInt(ByteOrder.LITTLE_ENDIAN) != 0 || buffer.getInt(ByteOrder.LITTLE_ENDIAN) != 8) throw Exception("Read Failed")

        val enumOffset = buffer.getInt(ByteOrder.LITTLE_ENDIAN)

        with(mutableListOf<IParamStatement>()) {
            buffer.getAsciiZ()
            for (i in 0 until buffer.getCompactInt())
                add(readStatement(file, buffer))

            file.slimCommands = this
        }

        if(!file.childrenOfType<RapClassImpl>().all { loadChildClasses(it, buffer) }) throw Exception()


        buffer.position(enumOffset) //TODO: ENUMS

        return file
    }



    fun readStatement(parent: IParamElement?, buffer: ByteBuffer): IParamStatement = when(buffer.get()) {
        0.toByte() -> RapClassImpl(parent, buffer.getAsciiZ(), buffer.getInt(ByteOrder.LITTLE_ENDIAN))

        1.toByte() -> RapVariableImpl(parent, ParamOperatorTypes.ASSIGN, buffer.getAsciiZ()).apply {
            paramValue = readLiteral(id = buffer.get().toInt(), parent = this@apply, buffer = buffer)
        }

        2.toByte() -> RapVariableImpl(parent, ParamOperatorTypes.ASSIGN, buffer.getAsciiZ()).apply {
            paramValue = readLiteral(this, buffer)
        }

        3.toByte() -> readExternalClass(parent, buffer)

        4.toByte() -> readDeleteStatement(parent, buffer)

        5.toByte() -> RapVariableImpl(parent, ParamOperatorTypes.forFlag(buffer.get()), buffer.getAsciiZ()).apply {
            paramValue = readArray(parent, buffer)
        }
        else -> throw Exception()
    }

    fun readLiteral(parent: IParamElement?, id: Int, buffer: ByteBuffer): IParamLiteralBase = when (id) {
        0 -> readString(parent, buffer)
        1 -> readFloat(parent, buffer)
        2 -> readInt(parent, buffer)
        3 -> readArray(parent, buffer)
        else -> throw Exception()
    }

    fun readLiteral(parent: IParamElement?, buffer: ByteBuffer): IParamLiteralBase = readLiteral(parent, buffer.getInt(), buffer)
}