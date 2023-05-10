package com.flipperplz.bisutils.param.node

interface RapStatementHolder : RapElement {
    val slimCommands: List<RapStatement>

    override val slimCurrentlyValid: Boolean
        get() = slimCommands.all { it.slimCurrentlyValid }

    override fun toEnforce(): String = slimCommands.joinToString(separator = "\n", postfix = "\n"){ it.toEnforce() }

}