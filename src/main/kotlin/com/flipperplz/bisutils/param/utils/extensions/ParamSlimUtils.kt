package com.flipperplz.bisutils.param.utils.extensions

import com.flipperplz.bisutils.param.IParamFile
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
    override val slimParent: IParamElement?,
    override val slimOperator: ParamOperatorTypes?,
    override val slimName: String?,
    ) : IParamVariableStatement {
        override lateinit var slimValue: IParamLiteralBase

        override val containingParamFile: IParamFile? = slimParent?.containingParamFile
    }

    internal class RapFileImpl(override val slimName: String?) : IParamFile {
        override lateinit var slimCommands: List<IParamStatement>
    }

    internal class RapClassImpl(
        override val slimParent: IParamElement?,
        override val slimName: String?,
        var binaryOffset: Int,
    ) : IParamClass {
        override lateinit var slimCommands: List<IParamStatement>
        override lateinit var  slimSuperClass: String
        override val containingParamFile: IParamFile? = slimParent?.containingParamFile

        override fun shouldValidateSuper(): Boolean = false
        override fun locateSuperClass(): IParamExternalClass? = null
    }

    inline fun IParamElement?.stringOf(crossinline value: () -> String): IParamString =
        IParamString(this, value())

    inline fun IParamElement?.mutableStringOf(crossinline value: () -> String): ParamMutableString =
        ParamMutableStringImpl(this, this?.containingParamFile, ParamStringType.QUOTED, value())

    inline fun mutableParamStringOf(parent: IParamElement? = null, crossinline value: () -> String): ParamMutableString =
        ParamMutableStringImpl(parent, parent?.containingParamFile, ParamStringType.QUOTED, value())

    inline fun paramStringOf(parent: IParamElement? = null, crossinline value: () -> String): IParamString =
        IParamString(parent, value())

    inline fun IParamElement?.intOf(crossinline value: () -> Int): IParamInt =
        IParamInt(this, value())

    inline fun IParamElement?.mutableIntOf(crossinline value: () -> Int): ParamMutableInt =
        ParamMutableIntImpl(this, this?.containingParamFile, value())

    inline fun mutableParamIntOf(parent: IParamElement? = null, crossinline value: () -> Int): ParamMutableInt =
        ParamMutableIntImpl(parent, parent?.containingParamFile, value())

    inline fun paramIntOf(parent: IParamElement? = null, crossinline value: () -> Int): IParamInt =
        IParamInt(parent, value())

    fun mutableParamFile(name: String): ParamMutableFile = ParamMutableFileImpl(name)

    fun mutableStringTable(): StringTableMutableFile = StringTableMutableFileImpl()

    inline fun IParamElement?.floatOf(crossinline value: () -> Float): IParamFloat =
        IParamFloat(this, value())

    inline fun IParamElement?.mutableFloatOf(crossinline value: () -> Float): ParamMutableFloat =
        ParamMutableFloatImpl(this, this?.containingParamFile, value())

    inline fun mutableParamFloatOf(parent: IParamElement? = null, crossinline value: () -> Float): ParamMutableFloat =
        ParamMutableFloatImpl(parent, parent?.containingParamFile, value())

    inline fun paramFloatOf(parent: IParamElement? = null, crossinline value: () -> Float): IParamFloat =
        IParamFloat(parent, value())

    inline fun IParamElement?.arrayOf(crossinline value: () -> List<IParamLiteralBase>): IParamArray =
        IParamArray(this, value())

    inline fun IParamElement?.mutableArrayOf(crossinline value: () -> MutableList<IParamLiteralBase>): ParamMutableArray =
        ParamMutableArrayImpl(this, this?.containingParamFile, value())

    inline fun mutableParamArrayOf(parent: IParamElement? = null, crossinline value: () -> MutableList<IParamLiteralBase>): ParamMutableArray =
        ParamMutableArrayImpl(parent, parent?.containingParamFile, value())

    inline fun paramArrayOf(parent: IParamElement? = null, crossinline value: () -> List<IParamLiteralBase>): IParamArray =
        IParamArray(parent, value())

    operator fun ParamMutableArray.plusAssign(push: IParamLiteralBase?) { push?.let { slimValue?.add(it) } }

    operator fun IParamFile.Companion.invoke(lexer: ParamLexer, name: String, preProcessor: BoostPreprocessor? = null): IParamFile =
        ParamParser.parse(lexer, name, preProcessor)

    fun parseParamFile(lexer: ParamLexer, name: String, preProcessor: BoostPreprocessor? = null): IParamFile =
        ParamParser.parse(lexer, name, preProcessor)

    fun paramLexerOf(buffer: ByteBuffer, charset: Charset = Charsets.UTF_8): ParamLexer =
        ParamLexer(buffer.array().toString(charset))

    fun openParamFile(buffer: ByteBuffer, name: String, preProcessor: BoostPreprocessor? = null): IParamFile =
        if(buffer[0] == 0.toByte() && buffer[1] == 114.toByte() && buffer[2] == 97.toByte() && buffer[3] == 80.toByte())
            ParamDebinarizer.readParamFile(name, buffer)
        else parseParamFile(paramLexerOf(buffer), name, preProcessor)

    inline operator fun <reified T : IParamNamedElement> IParamStatementHolder.get(name: String): T? =
        slimCommands.filterIsInstance<T>().firstOrNull {
            it.slimName.equals(name, true)
        }

    operator fun IParamStatementHolder.get(name: String): IParamStatement? =
        slimCommands.firstOrNull {
            it is IParamNamedElement && it.slimName.equals(name, true)
        }

    operator fun IParamStatementHolder.rem(name: String): IParamClass? =
        childrenOfType<IParamClass>().firstOrNull() {
            if(name == "*") return it
            it.slimName.equals(name, true)
        }
    //TODO(RYANN): order should be made, first deletes then classes then variables then preprocessor shit
    infix fun IParamStatementHolder.contains(name: String): Boolean =
        get(name) == null

    infix fun IParamStatementHolder.contains(command: IParamStatement): Boolean =
        slimCommands.contains(command)

    inline fun <reified T : IParamElement> IParamStatementHolder.childrenOfType(): List<T> =
        slimCommands.filterIsInstance<T>()


    val IParamStatementHolder.childClasses: List<IParamClass>
        get() = childrenOfType()

    operator fun IParamStatementHolder.iterator(): Iterator<IParamStatement> =
        slimCommands.iterator()

    operator fun IParamString.Companion.invoke(parent: IParamElement?, value: String): IParamString = object : IParamString {
        override val slimStringType: ParamStringType = ParamStringType.QUOTED
        override val slimValue: String = value
        override val slimParent: IParamElement? = parent
        override val containingParamFile: IParamFile? = parent?.containingParamFile
        override val length: Int = slimValue.length
        override fun get(index: Int): Char = slimValue[index]
        override fun subSequence(startIndex: Int, endIndex: Int): CharSequence = slimValue.subSequence(startIndex, endIndex)
    }

    operator fun IParamClass.Companion.invoke(
        parent: IParamElement?,
        classname: String,
        externalClassName: String? = null,
        commands: List<IParamStatement> = emptyList<IParamStatement>(),
        locateSuper: ((IParamClass) -> IParamExternalClass)? = null
    ): IParamClass = object : IParamClass {
        override val slimSuperClass: String? = externalClassName
        override fun locateSuperClass(): IParamExternalClass? = locateSuper?.let { it(this) }
        override fun shouldValidateSuper(): Boolean = locateSuper != null
        override val slimParent: IParamElement? = parent
        override val containingParamFile: IParamFile? = parent?.containingParamFile
        override val slimName: String = classname
        override val slimCommands: List<IParamStatement> = commands
    }

    operator fun IParamVariableStatement.Companion.invoke(
        parent: IParamElement?,
        name: String,
        operator: ParamOperatorTypes,
        value: IParamLiteralBase
    ): IParamVariableStatement = object : IParamVariableStatement {
        override val slimValue: IParamLiteralBase = value
        override val slimOperator: ParamOperatorTypes = operator
        override val slimParent: IParamElement? = parent
        override val containingParamFile: IParamFile? = parent?.containingParamFile
        override val slimName: String = name
    }

    operator fun IParamDeleteStatement.Companion.invoke(
        parent: IParamElement?,
        value: String,
        locateTarget: ((IParamDeleteStatement) -> IParamExternalClass)? = null
    ): IParamDeleteStatement = object : IParamDeleteStatement {
        override val slimName: String = value
        override fun locateTargetClass(): IParamExternalClass? = locateTarget?.let { it(this) }
        override fun shouldValidateTarget(): Boolean = locateTarget != null
        override val slimParent: IParamElement? = parent
        override val containingParamFile: IParamFile? = parent?.containingParamFile
    }

    operator fun IParamExternalClass.Companion.invoke(
        parent: IParamElement?,
        name: String
    ): IParamExternalClass = object : IParamExternalClass {
        override val slimName: String = name
        override val slimParent: IParamElement? = parent
        override val containingParamFile: IParamFile? = parent?.containingParamFile
    }

    operator fun IParamFloat.Companion.invoke(parent: IParamElement?, value: Float): IParamFloat = object : IParamFloat {
        override val slimValue: Float = value
        override val slimParent: IParamElement? = parent
        override val containingParamFile: IParamFile? = parent?.containingParamFile
    }

    operator fun IParamInt.Companion.invoke(parent: IParamElement?, value: Int): IParamInt = object : IParamInt {
        override val slimValue: Int = value
        override val slimParent: IParamElement? = parent
        override val containingParamFile: IParamFile? = parent?.containingParamFile
    }

    operator fun IParamArray.Companion.invoke(parent: IParamElement?, value: List<IParamLiteralBase>): IParamArray = object : IParamArray {
        override val slimValue: List<IParamLiteralBase> = value
        override val slimParent: IParamElement? = parent
        override val containingParamFile: IParamFile? = parent?.containingParamFile
        override val size: Int
            get() = slimValue.size

        override fun contains(element: IParamLiteralBase): Boolean = slimValue.contains(element)
        override fun containsAll(elements: Collection<IParamLiteralBase>): Boolean = slimValue.containsAll(elements)
        override fun get(index: Int): IParamLiteralBase = slimValue[index]
        override fun indexOf(element: IParamLiteralBase): Int = slimValue.indexOf(element)
        override fun isEmpty(): Boolean = slimValue.isEmpty()
        override fun iterator(): Iterator<IParamLiteralBase> = slimValue.iterator()
        override fun lastIndexOf(element: IParamLiteralBase): Int = slimValue.lastIndexOf(element)
        override fun listIterator(): ListIterator<IParamLiteralBase> = slimValue.listIterator()
        override fun listIterator(index: Int): ListIterator<IParamLiteralBase> = slimValue.listIterator(index)
        override fun subList(fromIndex: Int, toIndex: Int): List<IParamLiteralBase> = slimValue.subList(fromIndex, toIndex)
    }



    fun IParamStatementHolder.findElement(name: String): IParamNamedElement? {
        childrenOfType<IParamNamedElement>().forEach {
            if(it is IParamStatementHolder) it.findElement(name)?.let { found ->
                return found
            }
            if(name.equals(name, false)) return it
        }
        return null
    }

    fun IParamLiteralBase.isBlank(): Boolean = when(this) {
        is IParamString -> slimValue.isNullOrBlank()
        is IParamArray -> isEmpty()
        else -> false
    }



    fun IParamArray.toMutableArray(): ParamMutableArray =
        ParamMutableArrayImpl(slimParent, slimParent?.containingParamFile, slimValue?.toMutableList())

    fun IParamString.toMutableString(): ParamMutableString =
        ParamMutableStringImpl(slimParent, slimParent?.containingParamFile, slimStringType, slimValue)

    fun IParamFloat.toMutableFloat(): ParamMutableFloat =
        ParamMutableFloatImpl(slimParent, slimParent?.containingParamFile, slimValue)

    fun IParamInt.toMutableInt(): ParamMutableInt =
        ParamMutableIntImpl(slimParent, slimParent?.containingParamFile, slimValue)

    operator fun ParamMutableStatementHolder.plusAssign(statement: IParamStatement ) { slimCommands += statement }

    operator fun ParamMutableStatementHolder.minusAssign(statement: IParamStatement ) { slimCommands -= statement }

    fun IParamClass.toMutableClass(): ParamMutableClass = ParamMutableClassImpl(
        slimParent,
        slimParent?.containingParamFile,
        slimName,
        slimSuperClass,
        slimCommands.toMutableList(),
        false,
        null
    )

    fun IParamExternalClass.toMutableExternalClass(): ParamMutableExternalClass =
        ParamMutableExternalClassImpl(slimParent, slimParent?.containingParamFile, slimName)

    fun IParamVariableStatement.toMutableVariable(): ParamMutableVariableStatement =
        ParamMutableVariableStatementImpl(slimParent, slimParent?.containingParamFile, slimName, slimValue, slimOperator)

    fun IParamDeleteStatement.toMutableDeleteStatement(): ParamMutableDeleteStatement =
        ParamMutableDeleteStatementImpl(slimParent, slimParent?.containingParamFile, slimName)

    fun IParamFile.toMutableFile(): ParamMutableFile =
        ParamMutableFileImpl(slimName, slimCommands.toMutableList())
//}

