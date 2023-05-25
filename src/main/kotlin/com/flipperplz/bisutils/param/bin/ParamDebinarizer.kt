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
import com.flipperplz.bisutils.utils.getAsciiZ
import com.flipperplz.bisutils.utils.getCompactInt
import com.flipperplz.bisutils.utils.getFloat
import com.flipperplz.bisutils.utils.getInt
import java.nio.ByteBuffer
import java.nio.ByteOrder

object ParamDebinarizer {
    operator fun ParamString.Companion.invoke(parent: ParamElement?, buffer: ByteBuffer): ParamString =
            ParamString(parent, buffer.getAsciiZ())

    operator fun ParamFloat.Companion.invoke(parent: ParamElement?, buffer: ByteBuffer): ParamFloat =
            ParamFloat(parent, buffer.getFloat(ByteOrder.LITTLE_ENDIAN))

    operator fun ParamInt.Companion.invoke(parent: ParamElement?, buffer: ByteBuffer): ParamInt =
            ParamInt(parent, buffer.getInt(ByteOrder.LITTLE_ENDIAN))

    operator fun ParamExternalClass.Companion.invoke(parent: ParamElement?, buffer: ByteBuffer): ParamExternalClass =
            ParamExternalClass(parent, buffer.getAsciiZ())

    operator fun ParamDeleteStatement.Companion.invoke(
            parent: ParamElement?,
            buffer: ByteBuffer,
            locateTarget: ((ParamDeleteStatement) -> ParamExternalClass)? = null
    ): ParamDeleteStatement = ParamDeleteStatement(parent, buffer.getAsciiZ(), locateTarget)

    operator fun ParamArray.Companion.invoke(parent: ParamElement?, buffer: ByteBuffer): ParamArray = ParamArray(
            parent,
            mutableListOf<ParamLiteralBase>().apply {
                repeat(buffer.getCompactInt()) {
                    add(ParamLiteralBase(parent, buffer) ?: throw Exception())
                }
            }
    )


    fun readParamFile(name: String, buffer: ByteBuffer): ParamFile {

        val file = RapFileImpl(name)
        fun loadChildClasses(child: RapClassImpl, buffer: ByteBuffer): Boolean {
            with(mutableListOf<ParamStatement>()) {
                buffer.position(child.binaryOffset)
                child.slimSuperClass = buffer.getAsciiZ()

                for (i in 0 until buffer.getCompactInt())
                    add(ParamStatement(child, buffer) ?: return false)

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
                add(ParamStatement(file, buffer) ?: throw Exception())

            file.slimCommands = this
        }

        if(!file.childrenOfType<RapClassImpl>().all { loadChildClasses(it, buffer) }) throw Exception()


        buffer.position(enumOffset) //TODO: ENUMS

        return file
    }



    operator fun ParamStatement.Companion.invoke(parent: ParamElement?, buffer: ByteBuffer): ParamStatement? = when(buffer.get()) {
        0.toByte() -> RapClassImpl(parent, buffer.getAsciiZ(), buffer.getInt(ByteOrder.LITTLE_ENDIAN))
        1.toByte() -> with(buffer.get().toInt()) {
            RapVariableImpl(parent, ParamOperatorTypes.ASSIGN, buffer.getAsciiZ()).apply {
                slimValue = ParamLiteralBase.readStatement(this@apply, this@with, buffer) ?: throw Exception()
            }
        }
        2.toByte() -> RapVariableImpl(parent, ParamOperatorTypes.ASSIGN, buffer.getAsciiZ()).apply {
            slimValue = ParamArray(this, buffer)
        }
        3.toByte() -> ParamExternalClass(parent, buffer)
        4.toByte() -> ParamDeleteStatement(parent, buffer)
        5.toByte() -> RapVariableImpl(parent, ParamOperatorTypes.forFlag(buffer.get()), buffer.getAsciiZ()).apply {
            slimValue = ParamArray(this, buffer)
        }
        else -> null
    }

    operator fun ParamLiteralBase.Companion.invoke(parent: ParamElement?, buffer: ByteBuffer): ParamLiteralBase? =
            ParamLiteralBase.readStatement(parent, buffer.getInt(), buffer)

    fun ParamLiteralBase.Companion.readStatement(parent: ParamElement?, id: Int, buffer: ByteBuffer): ParamLiteralBase? = when (id) {
        0 -> ParamString(parent, buffer)
        1 -> ParamFloat(parent, buffer)
        2 -> ParamInt(parent, buffer)
        3 -> ParamArray(parent, buffer)
        else -> null
    }
}