package com.flipperplz.bisutils.param.utils.extensions

import com.flipperplz.bisutils.param.RapFile
import com.flipperplz.bisutils.param.literal.RapArray
import com.flipperplz.bisutils.param.literal.RapFloat
import com.flipperplz.bisutils.param.literal.RapInt
import com.flipperplz.bisutils.param.literal.RapString
import com.flipperplz.bisutils.param.node.*
import com.flipperplz.bisutils.param.statement.RapClass
import com.flipperplz.bisutils.param.statement.RapDeleteStatement
import com.flipperplz.bisutils.param.statement.RapExternalClass
import com.flipperplz.bisutils.param.utils.ParamStringType
import com.flipperplz.bisutils.utils.getAsciiZ
import com.flipperplz.bisutils.utils.getCompactInt
import com.flipperplz.bisutils.utils.getFloat
import com.flipperplz.bisutils.utils.getInt
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible

inline fun RapElement?.createValue(crossinline value: () -> String): RapString = RapString(this, value())

inline fun createRapValue(parent: RapElement? = null, crossinline value: () -> String): RapString = RapString(parent, value())

inline fun RapElement?.createValue(crossinline value: () -> Int): RapInt = RapInt(this, value())

inline fun createRapValue(parent: RapElement? = null, crossinline value: () -> Int): RapInt = RapInt(parent, value())

inline fun RapElement?.createValue(crossinline value: () -> Float): RapFloat = RapFloat(this, value())

inline fun createRapValue(parent: RapElement? = null, crossinline value: () -> Float): RapFloat = RapFloat(parent, value())

inline fun RapElement?.createValue(crossinline value: () -> List<RapLiteralBase>): RapArray = RapArray(this, value())

inline fun createRapValue(parent: RapElement? = null, crossinline value: () -> List<RapLiteralBase>): RapArray = RapArray(parent, value())

inline operator fun <reified T : RapNamedElement> RapStatementHolder.get(name: String): T? = slimCommands.filterIsInstance<T>().firstOrNull { it.slimName.equals(name, true) }

operator fun RapStatementHolder.get(name: String): RapStatement? = slimCommands.firstOrNull { it is RapNamedElement && it.slimName.equals(name, true) }

operator fun RapStatementHolder.rem(name: String): RapClass = slimCommands.filterIsInstance<RapClass>().first { it.slimName.equals(name, true) }

//TODO(RYANN): order should be made, first deletes then classes then variables then preprocessor shit
infix fun RapStatementHolder.contains(name: String): Boolean = get(name) == null

infix fun RapStatementHolder.contains(command: RapStatement): Boolean = slimCommands.contains(command)

inline fun <reified T : RapStatement> RapStatementHolder.childrenOfType(): List<T> = slimCommands.filterIsInstance<T>()

fun RapStatementHolder.childClasses(): List<RapClass> = childrenOfType()

operator fun RapArray.get(index: Int): RapLiteralBase? = slimValue?.get(index)

operator fun RapStatementHolder.iterator(): Iterator<RapStatement> = slimCommands.iterator()

operator fun RapArray.iterator(): Iterator<RapLiteralBase> = slimValue?.iterator() ?: iterator { }

operator fun RapString.Companion.invoke(parent: RapElement?, buffer: ByteBuffer): RapString = RapString(parent, buffer.getAsciiZ())

operator fun RapFloat.Companion.invoke(parent: RapElement?, buffer: ByteBuffer): RapFloat = RapFloat(parent, buffer.getFloat(ByteOrder.LITTLE_ENDIAN))

operator fun RapInt.Companion.invoke(parent: RapElement?, buffer: ByteBuffer): RapInt = RapInt(parent, buffer.getInt(ByteOrder.LITTLE_ENDIAN))

operator fun RapExternalClass.Companion.invoke(parent: RapElement?, buffer: ByteBuffer): RapExternalClass = RapExternalClass(parent, buffer.getAsciiZ())

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

operator fun RapStatement.Companion.invoke(parent: RapElement?, buffer: ByteBuffer): RapStatement? = when(buffer.get()) {
    0.toByte() -> TODO() // RapClass()
    1.toByte() -> TODO() // RapVariableStatement
    2.toByte() -> TODO() // RapArrayStatement
    3.toByte() -> RapExternalClass(parent, buffer)
    4.toByte() -> RapDeleteStatement(parent, buffer)
    5.toByte() -> TODO() // RapFlaggedArrayStatement
    else -> null
}

operator fun RapLiteralBase.Companion.invoke(parent: RapElement?, buffer: ByteBuffer): RapLiteralBase? = when (buffer.get()) {
    0.toByte() -> RapString(parent, buffer)
    1.toByte() -> RapFloat(parent, buffer)
    2.toByte() -> RapInt(parent, buffer)
    3.toByte() -> RapArray(parent, buffer)
    else -> null
}