package com.flipperplz.bisutils.param.node

import com.flipperplz.bisutils.param.utils.ParamElementTypes

/**
 * Base implementation of a ParamFile contrary to the name.
 *
 * @property fileName used in the process of config joining, usually this is config besides param files that are used to
 * define materials or custom configuration for a game addon
 */
interface RapFile : RapStatementHolder {
    val fileName: String
    val slimEnum: Map<String, Int>

    override fun getRapElementType(): ParamElementTypes = ParamElementTypes.FILE

    override fun toParam(): String = super.writeSlimCommands() + slimEnum.asSequence().joinToString(
        prefix = "enum {\n",
        postfix = "\n};",
        separator = ",\n"
    ) { "${it.key} = ${it.value}" }
}