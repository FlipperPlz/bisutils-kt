package com.flipperplz.bisutils.param.utils.extensions

import com.flipperplz.bisutils.param.RapFile
import com.flipperplz.bisutils.utils.*
import com.flipperplz.bisutils.param.literal.*
import com.flipperplz.bisutils.param.node.*
import com.flipperplz.bisutils.param.statement.*
import com.flipperplz.bisutils.param.utils.ParamOperatorTypes
import com.flipperplz.bisutils.param.utils.ParamStringType
import com.flipperplz.bisutils.param.utils.mutability.*
import java.nio.ByteBuffer
import java.nio.ByteOrder

object ParamSlimUtils {
    inline fun RapElement?.valueOf(crossinline value: () -> String): RapString =
        RapString(this, value())

    inline fun RapElement?.mutableValueOf(crossinline value: () -> String): ParamMutableString =
        ParamMutableString(this, ParamStringType.QUOTED, value())

    inline fun mutableParamValueOf(parent: RapElement? = null, crossinline value: () -> String): ParamMutableString =
        ParamMutableString(parent, ParamStringType.QUOTED, value())

    inline fun paramValueOf(parent: RapElement? = null, crossinline value: () -> String): RapString =
        RapString(parent, value())

    inline fun RapElement?.valueOf(crossinline value: () -> Int): RapInt =
        RapInt(this, value())

    inline fun RapElement?.mutableValueOf(crossinline value: () -> Int): ParamMutableInt =
        ParamMutableInt(this, value())

    inline fun mutableParamValueOf(parent: RapElement? = null, crossinline value: () -> Int): ParamMutableInt =
        ParamMutableInt(parent, value())

    inline fun paramValueOf(parent: RapElement? = null, crossinline value: () -> Int): RapInt =
        RapInt(parent, value())

    inline fun RapElement?.valueOf(crossinline value: () -> Float): RapFloat =
        RapFloat(this, value())

    inline fun RapElement?.mutableValueOf(crossinline value: () -> Float): ParamMutableFloat =
        ParamMutableFloat(this, value())

    inline fun mutableParamValueOf(parent: RapElement? = null, crossinline value: () -> Float): ParamMutableFloat =
        ParamMutableFloat(parent, value())

    inline fun paramValueOf(parent: RapElement? = null, crossinline value: () -> Float): RapFloat =
        RapFloat(parent, value())

    inline fun RapElement?.valueOf(crossinline value: () -> List<RapLiteralBase>): RapArray =
        RapArray(this, value())

    inline fun RapElement?.mutableValueOf(crossinline value: () -> MutableList<RapLiteralBase>): ParamMutableArray =
        ParamMutableArray(this, value())

    inline fun mutableParamValueOf(parent: RapElement? = null, crossinline value: () -> MutableList<RapLiteralBase>): ParamMutableArray =
        ParamMutableArray(parent, value())

    inline fun paramValueOf(parent: RapElement? = null, crossinline value: () -> List<RapLiteralBase>): RapArray =
        RapArray(parent, value())

    inline operator fun <reified T : RapNamedElement> RapStatementHolder.get(name: String): T? =
        slimCommands.filterIsInstance<T>().firstOrNull {
            it.slimName.equals(name, true)
        }

    operator fun RapStatementHolder.get(name: String): RapStatement? =
        slimCommands.firstOrNull {
            it is RapNamedElement && it.slimName.equals(name, true)
        }

    operator fun RapStatementHolder.rem(name: String): RapClass =
        slimCommands.filterIsInstance<RapClass>().first {
            it.slimName.equals(name, true)
        }

    //TODO(RYANN): order should be made, first deletes then classes then variables then preprocessor shit
    infix fun RapStatementHolder.contains(name: String): Boolean =
        get(name) == null

    infix fun RapStatementHolder.contains(command: RapStatement): Boolean =
        slimCommands.contains(command)

    inline fun <reified T : RapStatement> RapStatementHolder.childrenOfType(): List<T> =
        slimCommands.filterIsInstance<T>()

    fun RapStatementHolder.childClasses(): List<RapClass> =
        childrenOfType()

    operator fun RapArray.get(index: Int): RapLiteralBase? =
        slimValue?.get(index)

    operator fun RapStatementHolder.iterator(): Iterator<RapStatement> =
        slimCommands.iterator()

    operator fun RapArray.iterator(): Iterator<RapLiteralBase> =
        slimValue?.iterator() ?: iterator { }

    operator fun RapString.Companion.invoke(parent: RapElement?, buffer: ByteBuffer): RapString =
        RapString(parent, buffer.getAsciiZ())

    operator fun RapFloat.Companion.invoke(parent: RapElement?, buffer: ByteBuffer): RapFloat =
        RapFloat(parent, buffer.getFloat(ByteOrder.LITTLE_ENDIAN))

    operator fun RapInt.Companion.invoke(parent: RapElement?, buffer: ByteBuffer): RapInt =
        RapInt(parent, buffer.getInt(ByteOrder.LITTLE_ENDIAN))

    operator fun RapExternalClass.Companion.invoke(parent: RapElement?, buffer: ByteBuffer): RapExternalClass =
        RapExternalClass(parent, buffer.getAsciiZ())

    operator fun RapDeleteStatement.Companion.invoke(
        parent: RapElement?,
        buffer: ByteBuffer,
        locateTarget: ((RapDeleteStatement) -> RapExternalClass)? = null
    ): RapDeleteStatement =
        RapDeleteStatement(parent, buffer.getAsciiZ(), locateTarget)

    operator fun RapString.Companion.invoke(parent: RapElement?, value: String): RapString = object : RapString {
        override val slimStringType: ParamStringType = ParamStringType.QUOTED
        override val slimValue: String = value
        override val slimParent: RapElement? = parent
        override val containingFile: RapFile? = parent?.containingFile
    }

    operator fun RapClass.Companion.invoke(
        parent: RapElement?,
        classname: String,
        externalClassName: String? = null,
        commands: List<RapStatement> = emptyList<RapStatement>(),
        locateSuper: ((RapClass) -> RapExternalClass)? = null
    ): RapClass = object : RapClass {
        override val slimSuperClass: String? = externalClassName
        override fun locateSuperClass(): RapExternalClass? = locateSuper?.let { it(this) }
        override fun shouldValidateSuper(): Boolean = locateSuper != null
        override val slimParent: RapElement? = parent
        override val containingFile: RapFile? = parent?.containingFile
        override val slimName: String = classname
        override val slimCommands: List<RapStatement> = commands
    }

    operator fun RapVariableStatement.Companion.invoke(
        parent: RapElement?,
        name: String,
        operator: ParamOperatorTypes,
        value: RapLiteralBase
    ): RapVariableStatement = object : RapVariableStatement {
        override val slimValue: RapLiteralBase = value
        override val slimOperator: ParamOperatorTypes = operator
        override val slimParent: RapElement? = parent
        override val containingFile: RapFile? = parent?.containingFile
        override val slimName: String = name
    }

    operator fun RapDeleteStatement.Companion.invoke(
        parent: RapElement?,
        value: String,
        locateTarget: ((RapDeleteStatement) -> RapExternalClass)? = null
    ): RapDeleteStatement = object : RapDeleteStatement {
        override val slimName: String = value
        override fun locateTargetClass(): RapExternalClass? = locateTarget?.let { it(this) }
        override fun shouldValidateTarget(): Boolean = locateTarget != null
        override val slimParent: RapElement? = parent
        override val containingFile: RapFile? = parent?.containingFile
    }

    operator fun RapExternalClass.Companion.invoke(
        parent: RapElement?,
        name: String
    ): RapExternalClass = object : RapExternalClass {
        override val slimName: String = name
        override val slimParent: RapElement? = parent
        override val containingFile: RapFile? = parent?.containingFile
    }

    operator fun RapFloat.Companion.invoke(parent: RapElement?, value: Float): RapFloat = object : RapFloat {
        override val slimValue: Float = value
        override val slimParent: RapElement? = parent
        override val containingFile: RapFile? = parent?.containingFile
    }

    operator fun RapInt.Companion.invoke(parent: RapElement?, value: Int): RapInt = object : RapInt {
        override val slimValue: Int = value
        override val slimParent: RapElement? = parent
        override val containingFile: RapFile? = parent?.containingFile
    }

    operator fun RapArray.Companion.invoke(parent: RapElement?, value: List<RapLiteralBase>): RapArray = object : RapArray {
        override val slimValue: List<RapLiteralBase> = value
        override val slimParent: RapElement? = parent
        override val containingFile: RapFile? = parent?.containingFile
    }

    operator fun RapArray.Companion.invoke(parent: RapElement?, buffer: ByteBuffer): RapArray = RapArray(
        parent,
        mutableListOf<RapLiteralBase>().apply {
            repeat(buffer.getCompactInt()) {
                add(RapLiteralBase(parent, buffer) ?: throw Exception())
            }
        }
    )

    internal class RapVariableImpl(
        override val slimParent: RapElement?,
        override val slimOperator: ParamOperatorTypes?,
        override val slimName: String?,
    ) : RapVariableStatement {
        override lateinit var slimValue: RapLiteralBase

        override val containingFile: RapFile? = slimParent?.containingFile
    }

    internal class RapFileImpl(override val fileName: String) : RapFile {
        override lateinit var slimCommands: List<RapStatement>
    }

    internal class RapClassImpl(
        override val slimParent: RapElement?,
        override val slimName: String?,
        var binaryOffset: Int,
    ) : RapClass {
        override lateinit var slimCommands: List<RapStatement>
        override lateinit var  slimSuperClass: String
        override val containingFile: RapFile? = slimParent?.containingFile

        override fun shouldValidateSuper(): Boolean = false
        override fun locateSuperClass(): RapExternalClass? = null
    }

    operator fun RapFile.Companion.invoke(name: String, buffer: ByteBuffer): RapFile {

        val file = RapFileImpl(name)
        fun loadChildClasses(child: RapClassImpl, buffer: ByteBuffer): Boolean {
            with(mutableListOf<RapStatement>()) {
                buffer.position(child.binaryOffset)
                child.slimSuperClass = buffer.getAsciiZ()

                for (i in 0 until buffer.getCompactInt())
                    add(RapStatement(child, buffer) ?: return false)

                child.slimCommands = this
            }

            return child.childrenOfType<RapClassImpl>().all { loadChildClasses(it, buffer) }
        }

        if(buffer.get() != 0.toByte() || buffer.get() != 114.toByte() || buffer.get() != 97.toByte() || buffer.get() != 80.toByte() ||
           buffer.getInt(ByteOrder.LITTLE_ENDIAN) != 0 || buffer.getInt(ByteOrder.LITTLE_ENDIAN) != 8) throw Exception("Read Failed")

        val enumOffset = buffer.getInt(ByteOrder.LITTLE_ENDIAN)

        with(mutableListOf<RapStatement>()) {
            buffer.getAsciiZ()
            for (i in 0 until buffer.getCompactInt())
                add(RapStatement(file, buffer) ?: throw Exception())

            file.slimCommands = this
        }

        if(!file.childrenOfType<RapClassImpl>().all { loadChildClasses(it, buffer) }) throw Exception()


        buffer.position(enumOffset) //TODO: ENUMS

        return file
    }

    operator fun RapStatement.Companion.invoke(parent: RapElement?, buffer: ByteBuffer): RapStatement? = when(buffer.get()) {
        0.toByte() -> RapClassImpl(parent, buffer.getAsciiZ(), buffer.getInt(ByteOrder.LITTLE_ENDIAN))
        1.toByte() -> with(buffer.get().toInt()) {
            RapVariableImpl(parent, ParamOperatorTypes.ASSIGN, buffer.getAsciiZ()).apply {
                slimValue = RapLiteralBase.readStatement(this@apply, this@with, buffer) ?: throw Exception()
            }
        }
        2.toByte() -> RapVariableImpl(parent, ParamOperatorTypes.ASSIGN, buffer.getAsciiZ()).apply {
            slimValue = RapArray(this, buffer)
        }
        3.toByte() -> RapExternalClass(parent, buffer)
        4.toByte() -> RapDeleteStatement(parent, buffer)
        5.toByte() -> RapVariableImpl(parent, ParamOperatorTypes.forFlag(buffer.get()), buffer.getAsciiZ()).apply {
            slimValue = RapArray(this, buffer)
        }
        else -> null
    }

    operator fun RapLiteralBase.Companion.invoke(parent: RapElement?, buffer: ByteBuffer): RapLiteralBase? =
        RapLiteralBase.readStatement(parent, buffer.getInt(), buffer)

    fun RapLiteralBase.Companion.readStatement(parent: RapElement?, id: Int, buffer: ByteBuffer): RapLiteralBase? = when (id) {
        0 -> RapString(parent, buffer)
        1 -> RapFloat(parent, buffer)
        2 -> RapInt(parent, buffer)
        3 -> RapArray(parent, buffer)
        else -> null
    }

    fun RapArray.toMutableArray(): ParamMutableArray =
        ParamMutableArray(slimParent, slimValue?.toMutableList())

    fun RapString.toMutableString(): ParamMutableString =
        ParamMutableString(slimParent, slimStringType, slimValue)

    fun RapFloat.toMutableFloat(): ParamMutableFloat =
        ParamMutableFloat(slimParent, slimValue)

    fun RapInt.toMutableInt(): ParamMutableInt =
        ParamMutableInt(slimParent, slimValue)

    fun RapClass.toMutableClass(): ParamMutableClass = ParamMutableClass(
        slimParent,
        slimName,
        slimSuperClass,
        slimCommands.toMutableList(),
        false,
        null
    )

    fun RapExternalClass.toMutableExternalClass(): ParamMutableExternalClass =
        ParamMutableExternalClass(slimParent, slimName)

    fun RapVariableStatement.toMutableVariable(): ParamMutableVariableStatement =
        ParamMutableVariableStatement(slimParent, slimName, slimValue, slimOperator)

    fun RapDeleteStatement.toMutableDeleteStatement(): ParamMutableDeleteStatement =
        ParamMutableDeleteStatement(slimParent, slimName)

    fun RapFile.toMutableFile(): ParamMutableFile =
        ParamMutableFile(fileName, slimCommands.toMutableList())
}

