package com.flipperplz.bisutils.param.slim.util.extensions

import com.flipperplz.bisutils.param.slim.commands.ParamSlimClass
import com.flipperplz.bisutils.param.slim.literals.ParamSlimArray
import com.flipperplz.bisutils.param.slim.literals.ParamSlimFloat
import com.flipperplz.bisutils.param.slim.literals.ParamSlimInt
import com.flipperplz.bisutils.param.slim.literals.ParamSlimString
import com.flipperplz.bisutils.param.slim.literals.impl.*
import com.flipperplz.bisutils.param.slim.node.*

inline fun ParamSlim?.createValue(value: () -> String): ParamSlimString =
    ParamSlimStringImpl(this, value())

inline operator fun ParamSlimArrayImpl.set(i: Int, element: ParamSlimLiteralBase) { slimValue[i] = element }

inline fun createValue(parent: ParamSlim? = null, value: () -> String): ParamSlimString =
    ParamSlimStringImpl(parent, value())

inline fun ParamSlim?.createValue(value: () -> Int): ParamSlimInt =
    ParamSlimIntImpl(this, value())

inline fun createValue(parent: ParamSlim? = null, value: () -> Int): ParamSlimInt =
    ParamSlimIntImpl(parent, value())

inline fun ParamSlim?.createValue(value: () -> Float): ParamSlimFloat =
    ParamSlimFloatImpl(this, value())

inline fun createValue(parent: ParamSlim? = null, value: () -> Float): ParamSlimFloat =
    ParamSlimFloatImpl(parent, value())

inline fun ParamSlim?.createValue(value: () -> MutableList<ParamSlimLiteralBase>): ParamSlimArray =
    ParamSlimArrayImpl(this, value())

inline fun createValue(parent: ParamSlim? = null, value: () -> MutableList<ParamSlimLiteralBase>): ParamSlimArray =
    ParamSlimArrayImpl(parent, value())

inline operator fun <reified T: ParamSlimNamed> ParamSlimCommandHolder.get(name: String): T? =
    slimCommands.filterIsInstance<T>().firstOrNull {it.slimName.equals(name, true) }

inline operator fun ParamSlimCommandHolder.get(name: String): ParamSlimCommand? =
    slimCommands.firstOrNull {it is ParamSlimNamed && it.slimName.equals(name, true) }

inline operator fun ParamSlimCommandHolder.rem(name: String): ParamSlimClass =
    slimCommands.filterIsInstance<ParamSlimClass>().first { it.slimName.equals(name, true) }

//TODO(RYANN): order should be made, first deletes then classes then variables then preprocessor shit
inline infix fun ParamSlimCommandHolder.contains(name: String): Boolean =
    get(name) == null

inline infix fun ParamSlimCommandHolder.contains(command: ParamSlimCommand): Boolean =
    slimCommands.contains(command)

inline fun <reified T: ParamSlimCommand> ParamSlimCommandHolder.childrenOfType(): List<T> =
    slimCommands.filterIsInstance<T>()

inline fun ParamSlimCommandHolder.childClasses(): List<ParamSlimClass> = childrenOfType()

inline operator fun ParamSlimArray.get(index: Int): ParamSlimLiteralBase? =
    slimValue?.get(index)

inline operator fun ParamSlimCommandHolder.iterator(): Iterator<ParamSlimCommand> =
    slimCommands.iterator()

inline operator fun ParamSlimArray.iterator(): Iterator<ParamSlimLiteralBase> =
    slimValue?.iterator() ?: iterator {  }

// DELEGATION

