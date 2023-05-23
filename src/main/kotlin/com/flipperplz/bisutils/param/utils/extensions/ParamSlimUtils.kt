package com.flipperplz.bisutils.param.utils.extensions

import com.flipperplz.bisutils.param.ParamFile
import com.flipperplz.bisutils.utils.*
import com.flipperplz.bisutils.param.ast.literal.*
import com.flipperplz.bisutils.param.ast.node.*
import com.flipperplz.bisutils.param.ast.statement.*
import com.flipperplz.bisutils.param.utils.ParamOperatorTypes
import com.flipperplz.bisutils.param.utils.ParamStringType
import com.flipperplz.bisutils.param.utils.mutability.*
import com.flipperplz.bisutils.param.utils.mutability.node.ParamMutableStatementHolder
import java.nio.ByteBuffer
import java.nio.ByteOrder

//object ParamSlimUtils {
    internal class RapVariableImpl(
        override val slimParent: ParamElement?,
        override val slimOperator: ParamOperatorTypes?,
        override val slimName: String?,
    ) : ParamVariableStatement {
        override lateinit var slimValue: ParamLiteralBase

        override val containingParamFile: ParamFile? = slimParent?.containingParamFile
    }

    internal class RapFileImpl(override val fileName: String) : ParamFile {
        override lateinit var slimCommands: List<ParamStatement>
    }

    internal class RapClassImpl(
        override val slimParent: ParamElement?,
        override val slimName: String?,
        var binaryOffset: Int,
    ) : ParamClass {
        override lateinit var slimCommands: List<ParamStatement>
        override lateinit var  slimSuperClass: String
        override val containingParamFile: ParamFile? = slimParent?.containingParamFile

        override fun shouldValidateSuper(): Boolean = false
        override fun locateSuperClass(): ParamExternalClass? = null
    }

    inline fun ParamElement?.valueOf(crossinline value: () -> String): ParamString =
        ParamString(this, value())

    inline fun ParamElement?.mutableValueOf(crossinline value: () -> String): ParamMutableString =
        ParamMutableStringImpl(this, this?.containingParamFile, ParamStringType.QUOTED, value())

    inline fun mutableParamValueOf(parent: ParamElement? = null, crossinline value: () -> String): ParamMutableString =
        ParamMutableStringImpl(parent, parent?.containingParamFile, ParamStringType.QUOTED, value())

    inline fun paramValueOf(parent: ParamElement? = null, crossinline value: () -> String): ParamString =
        ParamString(parent, value())

    inline fun ParamElement?.valueOf(crossinline value: () -> Int): ParamInt =
        ParamInt(this, value())

    inline fun ParamElement?.mutableValueOf(crossinline value: () -> Int): ParamMutableInt =
        ParamMutableIntImpl(this, this?.containingParamFile, value())

    inline fun mutableParamValueOf(parent: ParamElement? = null, crossinline value: () -> Int): ParamMutableInt =
        ParamMutableIntImpl(parent, parent?.containingParamFile, value())

    inline fun paramValueOf(parent: ParamElement? = null, crossinline value: () -> Int): ParamInt =
        ParamInt(parent, value())

    fun mutableParamFile(name: String): ParamMutableFile = ParamMutableFileImpl(name)

    inline fun ParamElement?.valueOf(crossinline value: () -> Float): ParamFloat =
        ParamFloat(this, value())

    inline fun ParamElement?.mutableValueOf(crossinline value: () -> Float): ParamMutableFloat =
        ParamMutableFloatImpl(this, this?.containingParamFile, value())

    inline fun mutableParamValueOf(parent: ParamElement? = null, crossinline value: () -> Float): ParamMutableFloat =
        ParamMutableFloatImpl(parent, parent?.containingParamFile, value())

    inline fun paramValueOf(parent: ParamElement? = null, crossinline value: () -> Float): ParamFloat =
        ParamFloat(parent, value())

    inline fun ParamElement?.valueOf(crossinline value: () -> List<ParamLiteralBase>): ParamArray =
        ParamArray(this, value())

    inline fun ParamElement?.mutableValueOf(crossinline value: () -> MutableList<ParamLiteralBase>): ParamMutableArray =
        ParamMutableArrayImpl(this, this?.containingParamFile, value())

    inline fun mutableParamValueOf(parent: ParamElement? = null, crossinline value: () -> MutableList<ParamLiteralBase>): ParamMutableArray =
        ParamMutableArrayImpl(parent, parent?.containingParamFile, value())

    inline fun paramValueOf(parent: ParamElement? = null, crossinline value: () -> List<ParamLiteralBase>): ParamArray =
        ParamArray(parent, value())

    operator fun ParamMutableArray.plusAssign(push: ParamLiteralBase?) { push?.let { slimValue?.add(it) } }

    inline operator fun <reified T : ParamNamedElement> ParamStatementHolder.get(name: String): T? =
        slimCommands.filterIsInstance<T>().firstOrNull {
            it.slimName.equals(name, true)
        }

    operator fun ParamStatementHolder.get(name: String): ParamStatement? =
        slimCommands.firstOrNull {
            it is ParamNamedElement && it.slimName.equals(name, true)
        }

    operator fun ParamStatementHolder.rem(name: String): ParamClass =
        slimCommands.filterIsInstance<ParamClass>().first {
            it.slimName.equals(name, true)
        }

    //TODO(RYANN): order should be made, first deletes then classes then variables then preprocessor shit
    infix fun ParamStatementHolder.contains(name: String): Boolean =
        get(name) == null

    infix fun ParamStatementHolder.contains(command: ParamStatement): Boolean =
        slimCommands.contains(command)

    inline fun <reified T : ParamElement> ParamStatementHolder.childrenOfType(): List<T> =
        slimCommands.filterIsInstance<T>()


    fun ParamStatementHolder.childClasses(): List<ParamClass> =
        childrenOfType()

    operator fun ParamArray.get(index: Int): ParamLiteralBase? =
        slimValue?.get(index)

    operator fun ParamStatementHolder.iterator(): Iterator<ParamStatement> =
        slimCommands.iterator()

    operator fun ParamArray.iterator(): Iterator<ParamLiteralBase> =
        slimValue?.iterator() ?: iterator { }

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
    ): ParamDeleteStatement =
        ParamDeleteStatement(parent, buffer.getAsciiZ(), locateTarget)

    operator fun ParamString.Companion.invoke(parent: ParamElement?, value: String): ParamString = object : ParamString {
        override val slimStringType: ParamStringType = ParamStringType.QUOTED
        override val slimValue: String = value
        override val slimParent: ParamElement? = parent
        override val containingParamFile: ParamFile? = parent?.containingParamFile
    }

    operator fun ParamClass.Companion.invoke(
        parent: ParamElement?,
        classname: String,
        externalClassName: String? = null,
        commands: List<ParamStatement> = emptyList<ParamStatement>(),
        locateSuper: ((ParamClass) -> ParamExternalClass)? = null
    ): ParamClass = object : ParamClass {
        override val slimSuperClass: String? = externalClassName
        override fun locateSuperClass(): ParamExternalClass? = locateSuper?.let { it(this) }
        override fun shouldValidateSuper(): Boolean = locateSuper != null
        override val slimParent: ParamElement? = parent
        override val containingParamFile: ParamFile? = parent?.containingParamFile
        override val slimName: String = classname
        override val slimCommands: List<ParamStatement> = commands
    }

    operator fun ParamVariableStatement.Companion.invoke(
        parent: ParamElement?,
        name: String,
        operator: ParamOperatorTypes,
        value: ParamLiteralBase
    ): ParamVariableStatement = object : ParamVariableStatement {
        override val slimValue: ParamLiteralBase = value
        override val slimOperator: ParamOperatorTypes = operator
        override val slimParent: ParamElement? = parent
        override val containingParamFile: ParamFile? = parent?.containingParamFile
        override val slimName: String = name
    }

    operator fun ParamDeleteStatement.Companion.invoke(
        parent: ParamElement?,
        value: String,
        locateTarget: ((ParamDeleteStatement) -> ParamExternalClass)? = null
    ): ParamDeleteStatement = object : ParamDeleteStatement {
        override val slimName: String = value
        override fun locateTargetClass(): ParamExternalClass? = locateTarget?.let { it(this) }
        override fun shouldValidateTarget(): Boolean = locateTarget != null
        override val slimParent: ParamElement? = parent
        override val containingParamFile: ParamFile? = parent?.containingParamFile
    }

    operator fun ParamExternalClass.Companion.invoke(
        parent: ParamElement?,
        name: String
    ): ParamExternalClass = object : ParamExternalClass {
        override val slimName: String = name
        override val slimParent: ParamElement? = parent
        override val containingParamFile: ParamFile? = parent?.containingParamFile
    }

    operator fun ParamFloat.Companion.invoke(parent: ParamElement?, value: Float): ParamFloat = object : ParamFloat {
        override val slimValue: Float = value
        override val slimParent: ParamElement? = parent
        override val containingParamFile: ParamFile? = parent?.containingParamFile
    }

    operator fun ParamInt.Companion.invoke(parent: ParamElement?, value: Int): ParamInt = object : ParamInt {
        override val slimValue: Int = value
        override val slimParent: ParamElement? = parent
        override val containingParamFile: ParamFile? = parent?.containingParamFile
    }

    operator fun ParamArray.Companion.invoke(parent: ParamElement?, value: List<ParamLiteralBase>): ParamArray = object : ParamArray {
        override val slimValue: List<ParamLiteralBase> = value
        override val slimParent: ParamElement? = parent
        override val containingParamFile: ParamFile? = parent?.containingParamFile
    }

    operator fun ParamArray.Companion.invoke(parent: ParamElement?, buffer: ByteBuffer): ParamArray = ParamArray(
        parent,
        mutableListOf<ParamLiteralBase>().apply {
            repeat(buffer.getCompactInt()) {
                add(ParamLiteralBase(parent, buffer) ?: throw Exception())
            }
        }
    )

    fun ParamStatementHolder.findElement(name: String): ParamNamedElement? {
        childrenOfType<ParamNamedElement>().forEach {
            if(it is ParamStatementHolder) it.findElement(name)?.let { found ->
                return found
            }
            if(name.equals(name, false)) return it
        }
        return null
    }

    operator fun ParamFile.Companion.invoke(name: String, buffer: ByteBuffer): ParamFile {

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

    fun ParamArray.toMutableArray(): ParamMutableArray =
        ParamMutableArrayImpl(slimParent, slimParent?.containingParamFile, slimValue?.toMutableList())

    fun ParamString.toMutableString(): ParamMutableString =
        ParamMutableStringImpl(slimParent, slimParent?.containingParamFile, slimStringType, slimValue)

    fun ParamFloat.toMutableFloat(): ParamMutableFloat =
        ParamMutableFloatImpl(slimParent, slimParent?.containingParamFile, slimValue)

    fun ParamInt.toMutableInt(): ParamMutableInt =
        ParamMutableIntImpl(slimParent, slimParent?.containingParamFile, slimValue)

    operator fun ParamMutableStatementHolder.plusAssign(statement: ParamStatement ) { slimCommands += statement }

    operator fun ParamMutableStatementHolder.minusAssign(statement: ParamStatement ) { slimCommands -= statement }

    fun ParamClass.toMutableClass(): ParamMutableClass = ParamMutableClassImpl(
        slimParent,
        slimParent?.containingParamFile,
        slimName,
        slimSuperClass,
        slimCommands.toMutableList(),
        false,
        null
    )

    fun ParamExternalClass.toMutableExternalClass(): ParamMutableExternalClass =
        ParamMutableExternalClassImpl(slimParent, slimParent?.containingParamFile, slimName)

    fun ParamVariableStatement.toMutableVariable(): ParamMutableVariableStatement =
        ParamMutableVariableStatementImpl(slimParent, slimParent?.containingParamFile, slimName, slimValue, slimOperator)

    fun ParamDeleteStatement.toMutableDeleteStatement(): ParamMutableDeleteStatement =
        ParamMutableDeleteStatementImpl(slimParent, slimParent?.containingParamFile, slimName)

    fun ParamFile.toMutableFile(): ParamMutableFile =
        ParamMutableFileImpl(fileName, slimCommands.toMutableList())
//}

