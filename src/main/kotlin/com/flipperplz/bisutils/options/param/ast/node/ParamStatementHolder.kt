package com.flipperplz.bisutils.options.param.ast.node

interface ParamStatementHolder : ParamElement {
    val slimCommands: List<ParamStatement>

    override fun isCurrentlyValid(): Boolean = slimCommands.all { it.isCurrentlyValid() }

    override fun isBinarizable(): Boolean = slimCommands.all { it.isBinarizable() }

    fun writeSlimCommands() = slimCommands.joinToString(separator = "\n", postfix = "\n") { it.toParam() }
}