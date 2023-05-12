package com.flipperplz.bisutils.param.utils.extensions

import com.flipperplz.bisutils.param.literal.RapArray
import com.flipperplz.bisutils.param.literal.RapFloat
import com.flipperplz.bisutils.param.literal.RapInt
import com.flipperplz.bisutils.param.literal.RapString
import com.flipperplz.bisutils.param.node.*
import com.flipperplz.bisutils.param.statement.RapClass
import com.flipperplz.bisutils.param.utils.ParamStringType
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible

inline fun RapElement?.createValue(crossinline value: () -> String): RapString =
    object : RapString {
        override var slimStringType: ParamStringType = ParamStringType.QUOTED
        override var slimValue: String? = value()
        override val slimParent: RapElement = this
    }

//operator fun MutableRapArrayImpl.set(i: Int, element: RapLiteralBase) {
//    slimValue[i] = element
//}
//
inline fun createRapValue(parent: RapElement? = null, crossinline value: () -> String): RapString =
    object : RapString {
        override var slimStringType: ParamStringType = ParamStringType.QUOTED
        override var slimValue: String? = value()
        override val slimParent: RapElement? = parent
    }

inline fun RapElement?.createValue(crossinline value: () -> Int): RapInt =
    object : RapInt {
        override val slimValue: Int = value()
        override val slimParent: RapElement = this
    }

inline fun createRapValue(parent: RapElement? = null, crossinline value: () -> Int): RapInt =
    object : RapInt {
        override val slimValue: Int = value()
        override val slimParent: RapElement? = parent
    }

inline fun RapElement?.createValue(crossinline value: () -> Float): RapFloat =
    object : RapFloat {
        override val slimValue: Float = value()
        override val slimParent: RapElement = this
    }

inline fun createRapValue(parent: RapElement? = null, crossinline value: () -> Float): RapFloat =
    object : RapFloat {
        override val slimValue: Float = value()
        override val slimParent: RapElement? = parent
    }

inline fun RapElement?.createValue(crossinline value: () -> List<RapLiteralBase>): RapArray =
    object : RapArray {
        override val slimValue: List<RapLiteralBase> = value()
        override val slimParent: RapElement = this
    }

inline fun createRapValue(parent: RapElement? = null, crossinline value: () -> List<RapLiteralBase>): RapArray =
    object : RapArray {
        override val slimValue: List<RapLiteralBase> = value()
        override val slimParent: RapElement? = parent
    }

inline operator fun <reified T : RapNamedElement> RapStatementHolder.get(name: String): T? =
    slimCommands.filterIsInstance<T>().firstOrNull { it.slimName.equals(name, true) }

operator fun RapStatementHolder.get(name: String): RapStatement? =
    slimCommands.firstOrNull { it is RapNamedElement && it.slimName.equals(name, true) }

operator fun RapStatementHolder.rem(name: String): RapClass =
    slimCommands.filterIsInstance<RapClass>().first { it.slimName.equals(name, true) }

//TODO(RYANN): order should be made, first deletes then classes then variables then preprocessor shit
infix fun RapStatementHolder.contains(name: String): Boolean =
    get(name) == null

infix fun RapStatementHolder.contains(command: RapStatement): Boolean =
    slimCommands.contains(command)

inline fun <reified T : RapStatement> RapStatementHolder.childrenOfType(): List<T> =
    slimCommands.filterIsInstance<T>()

fun RapStatementHolder.childClasses(): List<RapClass> = childrenOfType()

operator fun RapArray.get(index: Int): RapLiteralBase? =
    slimValue?.get(index)

operator fun RapStatementHolder.iterator(): Iterator<RapStatement> =
    slimCommands.iterator()

operator fun RapArray.iterator(): Iterator<RapLiteralBase> =
    slimValue?.iterator() ?: iterator { }
//
//operator fun RapLiteralBase.invoke(buffer: ByteBuffer, parent: RapElement?): RapLiteralBase? = when (buffer.get()) {
//    0.toByte() -> RapStringImpl(buffer, parent)
//    1.toByte() -> RapFloatImpl(buffer, parent)
//    2.toByte() -> RapIntImpl(buffer, parent)
//    3.toByte() -> RapArrayImpl(buffer, parent)
//    else -> null
//}