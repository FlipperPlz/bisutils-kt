package com.flipperplz.bisutils.param.slim.node

interface ParamSlimCommandHolder : ParamSlim {
    val slimCommands: List<ParamSlimCommand>

    override val slimCurrentlyValid: Boolean
        get() = slimCommands.all { it.slimCurrentlyValid }

    override fun toEnforce(): String = slimCommands.joinToString(separator = "\n", postfix = "\n"){ it.toEnforce() }
}