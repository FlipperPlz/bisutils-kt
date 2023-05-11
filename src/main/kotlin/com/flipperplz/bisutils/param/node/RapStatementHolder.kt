package com.flipperplz.bisutils.param.node

/**
 * Represents a param element that contains a list of statements (commands)
 * @see com.flipperplz.bisutils.param.statement.RapClass
 */
interface RapStatementHolder : RapElement {
    val slimCommands: List<RapStatement>

    override val slimCurrentlyValid: Boolean
        get() = slimCommands.all { it.slimCurrentlyValid }

    override fun toParam(): String = slimCommands.joinToString(separator = "\n", postfix = "\n") { it.toParam() }

}