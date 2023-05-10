package com.flipperplz.bisutils.param.utils.extensions

import com.flipperplz.bisutils.param.literal.RapArray
import com.flipperplz.bisutils.param.literal.RapFloat
import com.flipperplz.bisutils.param.literal.RapInt
import com.flipperplz.bisutils.param.literal.RapString
import com.flipperplz.bisutils.param.literal.impl.*
import com.flipperplz.bisutils.param.node.*
import com.flipperplz.bisutils.param.statement.RapClass
import java.nio.ByteBuffer

inline fun RapElement?.createValue(value: () -> String): RapString =
    ParamSlimStringImpl(this, value())

inline operator fun ParamSlimArrayImpl.set(i: Int, element: RapLiteralBase) { slimValue[i] = element }

inline fun createValue(parent: RapElement? = null, value: () -> String): RapString =
    ParamSlimStringImpl(parent, value())

inline fun RapElement?.createValue(value: () -> Int): RapInt =
    ParamSlimIntImpl(this, value())

inline fun createValue(parent: RapElement? = null, value: () -> Int): RapInt =
    ParamSlimIntImpl(parent, value())

inline fun RapElement?.createValue(value: () -> Float): RapFloat =
    ParamSlimFloatImpl(this, value())

inline fun createValue(parent: RapElement? = null, value: () -> Float): RapFloat =
    ParamSlimFloatImpl(parent, value())

inline fun RapElement?.createValue(value: () -> MutableList<RapLiteralBase>): RapArray =
    ParamSlimArrayImpl(this, value())

inline fun createValue(parent: RapElement? = null, value: () -> MutableList<RapLiteralBase>): RapArray =
    ParamSlimArrayImpl(parent, value())

inline operator fun <reified T: RapNamedElement> RapStatementHolder.get(name: String): T? =
    slimCommands.filterIsInstance<T>().firstOrNull {it.slimName.equals(name, true) }

inline operator fun RapStatementHolder.get(name: String): RapStatement? =
    slimCommands.firstOrNull {it is RapNamedElement && it.slimName.equals(name, true) }

inline operator fun RapStatementHolder.rem(name: String): RapClass =
    slimCommands.filterIsInstance<RapClass>().first { it.slimName.equals(name, true) }

//TODO(RYANN): order should be made, first deletes then classes then variables then preprocessor shit
inline infix fun RapStatementHolder.contains(name: String): Boolean =
    get(name) == null

inline infix fun RapStatementHolder.contains(command: RapStatement): Boolean =
    slimCommands.contains(command)

inline fun <reified T: RapStatement> RapStatementHolder.childrenOfType(): List<T> =
    slimCommands.filterIsInstance<T>()

inline fun RapStatementHolder.childClasses(): List<RapClass> = childrenOfType()

inline operator fun RapArray.get(index: Int): RapLiteralBase? =
    slimValue?.get(index)

inline operator fun RapStatementHolder.iterator(): Iterator<RapStatement> =
    slimCommands.iterator()

inline operator fun RapArray.iterator(): Iterator<RapLiteralBase> =
    slimValue?.iterator() ?: iterator {  }

operator fun RapLiteralBase.invoke(buffer: ByteBuffer, parent: RapElement?): RapLiteralBase? = when(buffer.get()) {
    0.toByte() -> RapStringImpl(buffer, parent)
    1.toByte() -> RapFloatImpl(buffer, parent)
    2.toByte() -> RapIntImpl(buffer, parent)
    3.toByte() -> RapArrayImpl(buffer, parent)
    else -> null
}

// DELEGATION

