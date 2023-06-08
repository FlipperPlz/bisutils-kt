package com.flipperplz.bisutils.param.utils.extensions

import com.flipperplz.bisutils.param.ParamFile
import com.flipperplz.bisutils.param.ast.literal.*
import com.flipperplz.bisutils.param.ast.node.*
import com.flipperplz.bisutils.param.ast.statement.*
import com.flipperplz.bisutils.param.bin.ParamDebinarizer
import com.flipperplz.bisutils.param.lexer.ParamLexer
import com.flipperplz.bisutils.param.parser.ParamParser
import com.flipperplz.bisutils.param.utils.ParamOperatorTypes
import com.flipperplz.bisutils.param.utils.ParamStringType
import com.flipperplz.bisutils.param.utils.mutability.*
import com.flipperplz.bisutils.param.utils.mutability.node.ParamMutableStatementHolder
import com.flipperplz.bisutils.preprocesser.boost.BoostPreprocessor
import com.flipperplz.bisutils.stringtable.ast.mutable.StringTableMutableFile
import com.flipperplz.bisutils.stringtable.astImpl.mutable.StringTableMutableFileImpl
import java.nio.ByteBuffer
import java.nio.charset.Charset

//object ParamSlimUtils {
    internal class RapVariableImpl(
        override val slimParent: ParamElement?,
        override val slimOperator: ParamOperatorTypes?,
        override val slimName: String?,
    ) : ParamVariableStatement {
        override lateinit var slimValue: ParamLiteralBase

        override val containingParamFile: ParamFile? = slimParent?.containingParamFile
    }

    internal class RapFileImpl(override val slimName: String?) : ParamFile {
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

    inline fun ParamElement?.stringOf(crossinline value: () -> String): ParamString =
        ParamString(this, value())

    inline fun ParamElement?.mutableStringOf(crossinline value: () -> String): ParamMutableString =
        ParamMutableStringImpl(this, this?.containingParamFile, ParamStringType.QUOTED, value())

    inline fun mutableParamStringOf(parent: ParamElement? = null, crossinline value: () -> String): ParamMutableString =
        ParamMutableStringImpl(parent, parent?.containingParamFile, ParamStringType.QUOTED, value())

    inline fun paramStringOf(parent: ParamElement? = null, crossinline value: () -> String): ParamString =
        ParamString(parent, value())

    inline fun ParamElement?.intOf(crossinline value: () -> Int): ParamInt =
        ParamInt(this, value())

    inline fun ParamElement?.mutableIntOf(crossinline value: () -> Int): ParamMutableInt =
        ParamMutableIntImpl(this, this?.containingParamFile, value())

    inline fun mutableParamIntOf(parent: ParamElement? = null, crossinline value: () -> Int): ParamMutableInt =
        ParamMutableIntImpl(parent, parent?.containingParamFile, value())

    inline fun paramIntOf(parent: ParamElement? = null, crossinline value: () -> Int): ParamInt =
        ParamInt(parent, value())

    fun mutableParamFile(name: String): ParamMutableFile = ParamMutableFileImpl(name)

    fun mutableStringTable(): StringTableMutableFile = StringTableMutableFileImpl()

    inline fun ParamElement?.floatOf(crossinline value: () -> Float): ParamFloat =
        ParamFloat(this, value())

    inline fun ParamElement?.mutableFloatOf(crossinline value: () -> Float): ParamMutableFloat =
        ParamMutableFloatImpl(this, this?.containingParamFile, value())

    inline fun mutableParamFloatOf(parent: ParamElement? = null, crossinline value: () -> Float): ParamMutableFloat =
        ParamMutableFloatImpl(parent, parent?.containingParamFile, value())

    inline fun paramFloatOf(parent: ParamElement? = null, crossinline value: () -> Float): ParamFloat =
        ParamFloat(parent, value())

    inline fun ParamElement?.arrayOf(crossinline value: () -> List<ParamLiteralBase>): ParamArray =
        ParamArray(this, value())

    inline fun ParamElement?.mutableArrayOf(crossinline value: () -> MutableList<ParamLiteralBase>): ParamMutableArray =
        ParamMutableArrayImpl(this, this?.containingParamFile, value())

    inline fun mutableParamArrayOf(parent: ParamElement? = null, crossinline value: () -> MutableList<ParamLiteralBase>): ParamMutableArray =
        ParamMutableArrayImpl(parent, parent?.containingParamFile, value())

    inline fun paramArrayOf(parent: ParamElement? = null, crossinline value: () -> List<ParamLiteralBase>): ParamArray =
        ParamArray(parent, value())

    operator fun ParamMutableArray.plusAssign(push: ParamLiteralBase?) { push?.let { slimValue?.add(it) } }

    operator fun ParamFile.Companion.invoke(lexer: ParamLexer, name: String, preProcessor: BoostPreprocessor? = null): ParamFile =
        ParamParser.parse(lexer, name, preProcessor)

    fun parseParamFile(lexer: ParamLexer, name: String, preProcessor: BoostPreprocessor? = null): ParamFile =
        ParamParser.parse(lexer, name, preProcessor)

    fun paramLexerOf(buffer: ByteBuffer, charset: Charset = Charsets.UTF_8): ParamLexer =
        ParamLexer(buffer.array().toString(charset))

    fun openParamFile(buffer: ByteBuffer, name: String, preProcessor: BoostPreprocessor? = null): ParamFile =
        if(buffer[0] == 0.toByte() && buffer[1] == 114.toByte() && buffer[2] == 97.toByte() && buffer[3] == 80.toByte())
            ParamDebinarizer.readParamFile(name, buffer)
        else parseParamFile(paramLexerOf(buffer), name, preProcessor)

    inline operator fun <reified T : ParamNamedElement> ParamStatementHolder.get(name: String): T? =
        slimCommands.filterIsInstance<T>().firstOrNull {
            it.slimName.equals(name, true)
        }

    operator fun ParamStatementHolder.get(name: String): ParamStatement? =
        slimCommands.firstOrNull {
            it is ParamNamedElement && it.slimName.equals(name, true)
        }

    operator fun ParamStatementHolder.rem(name: String): ParamClass? =
        childrenOfType<ParamClass>().firstOrNull() {
            if(name == "*") return it
            it.slimName.equals(name, true)
        }
    //TODO(RYANN): order should be made, first deletes then classes then variables then preprocessor shit
    infix fun ParamStatementHolder.contains(name: String): Boolean =
        get(name) == null

    infix fun ParamStatementHolder.contains(command: ParamStatement): Boolean =
        slimCommands.contains(command)

    inline fun <reified T : ParamElement> ParamStatementHolder.childrenOfType(): List<T> =
        slimCommands.filterIsInstance<T>()


    val ParamStatementHolder.childClasses: List<ParamClass>
        get() = childrenOfType()

    operator fun ParamStatementHolder.iterator(): Iterator<ParamStatement> =
        slimCommands.iterator()

    operator fun ParamString.Companion.invoke(parent: ParamElement?, value: String): ParamString = object : ParamString {
        override val slimStringType: ParamStringType = ParamStringType.QUOTED
        override val slimValue: String = value
        override val slimParent: ParamElement? = parent
        override val containingParamFile: ParamFile? = parent?.containingParamFile
        override val length: Int = slimValue.length
        override fun get(index: Int): Char = slimValue[index]
        override fun subSequence(startIndex: Int, endIndex: Int): CharSequence = slimValue.subSequence(startIndex, endIndex)
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
        override val size: Int
            get() = slimValue.size

        override fun contains(element: ParamLiteralBase): Boolean = slimValue.contains(element)
        override fun containsAll(elements: Collection<ParamLiteralBase>): Boolean = slimValue.containsAll(elements)
        override fun get(index: Int): ParamLiteralBase = slimValue[index]
        override fun indexOf(element: ParamLiteralBase): Int = slimValue.indexOf(element)
        override fun isEmpty(): Boolean = slimValue.isEmpty()
        override fun iterator(): Iterator<ParamLiteralBase> = slimValue.iterator()
        override fun lastIndexOf(element: ParamLiteralBase): Int = slimValue.lastIndexOf(element)
        override fun listIterator(): ListIterator<ParamLiteralBase> = slimValue.listIterator()
        override fun listIterator(index: Int): ListIterator<ParamLiteralBase> = slimValue.listIterator(index)
        override fun subList(fromIndex: Int, toIndex: Int): List<ParamLiteralBase> = slimValue.subList(fromIndex, toIndex)
    }



    fun ParamStatementHolder.findElement(name: String): ParamNamedElement? {
        childrenOfType<ParamNamedElement>().forEach {
            if(it is ParamStatementHolder) it.findElement(name)?.let { found ->
                return found
            }
            if(name.equals(name, false)) return it
        }
        return null
    }

    fun ParamLiteralBase.isBlank(): Boolean = when(this) {
        is ParamString -> slimValue.isNullOrBlank()
        is ParamArray -> isEmpty()
        else -> false
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
        ParamMutableFileImpl(slimName, slimCommands.toMutableList())
//}

