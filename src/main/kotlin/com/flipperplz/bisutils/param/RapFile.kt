package com.flipperplz.bisutils.param

import com.flipperplz.bisutils.param.node.RapStatementHolder
import com.flipperplz.bisutils.param.utils.ParamElementTypes

/**
 * Base implementation of a ParamFile contrary to the name.
 *
 * @property fileName used in the process of config joining, usually this is config besides param files that are used to
 * define materials or custom configuration for a game addon
 */
interface RapFile : RapStatementHolder {
    val fileName: String

    override fun getRapElementType(): ParamElementTypes = ParamElementTypes.FILE

    override fun toParam(): String = super.writeSlimCommands()
}