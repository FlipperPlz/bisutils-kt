package com.flipperplz.bisutils.param.node

/**
 * Represents a param element that contains a list of statements (commands)
 * @see com.flipperplz.bisutils.param.statement.RapClass
 */
interface RapStatementHolder : RapElement {
    val slimCommands: List<RapStatement>

    override fun isCurrentlyValid(): Boolean = slimCommands.all { it.isCurrentlyValid() }

    override fun isBinarizable(): Boolean = slimCommands.all { it.isBinarizable() }

    fun writeSlimCommands() = slimCommands.joinToString(separator = "\n", postfix = "\n") { it.toParam() }
}