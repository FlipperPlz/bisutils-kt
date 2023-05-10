package com.flipperplz.bisutils.param.slim.util

import com.flipperplz.bisutils.param.slim.commands.ParamSlimClass
import com.flipperplz.bisutils.param.slim.literals.ParamSlimArray
import com.flipperplz.bisutils.param.slim.node.ParamSlimCommand
import com.flipperplz.bisutils.param.slim.node.ParamSlimCommandHolder
import com.flipperplz.bisutils.param.slim.node.ParamSlimLiteral
import com.flipperplz.bisutils.param.slim.node.ParamSlimNamed


inline operator fun <reified T: ParamSlimNamed> ParamSlimCommandHolder.get(name: String): T? =
    slimCommands.filterIsInstance<T>().firstOrNull {it.slimName.equals(name, true) }

inline operator fun ParamSlimCommandHolder.get(name: String): ParamSlimCommand? =
    slimCommands.firstOrNull {it is ParamSlimNamed && it.slimName.equals(name, true) }

inline operator fun ParamSlimCommandHolder.rem(name: String): ParamSlimClass =
    slimCommands.filterIsInstance<ParamSlimClass>().first { it.slimName.equals(name, true) }

//TODO(RYANN): order should be made, first deletes then classes then variables then preprocessor shit
inline infix fun ParamSlimCommandHolder.contains(name: String): Boolean = get(name) == null

inline infix fun ParamSlimCommandHolder.contains(command: ParamSlimCommand): Boolean = slimCommands.contains(command)

inline fun <reified T: ParamSlimCommand> ParamSlimCommandHolder.childrenOfType(): List<T> =
    slimCommands.filterIsInstance<T>()

inline operator fun ParamSlimArray.get(index: Int): ParamSlimLiteral<*>? =
    slimValue?.get(index)

inline operator fun ParamSlimCommandHolder.iterator(): Iterator<ParamSlimCommand> =
    slimCommands.iterator()

inline operator fun ParamSlimArray.iterator(): Iterator<ParamSlimLiteral<*>> =
    slimValue?.iterator() ?: iterator {  }

// DELEGATION

