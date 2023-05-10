package com.flipperplz.bisutils.param.slim.node

/**
 * The ParamSlimCommandHolder interface is a marker interface that is used to indicate that a class is capable of
 * holding ParamSlim commands. Classes that implement this interface are expected to have a collection of
 * ParamSlimCommand objects.
 */
interface ParamSlimCommandHolder : ParamSlim {
    val slimCommands: List<ParamSlimCommand>

    override val slimCurrentlyValid: Boolean
        get() = slimCommands.all { it.slimCurrentlyValid }

    override fun toEnforce(): String = slimCommands.joinToString(separator = "\n", postfix = "\n"){ it.toEnforce() }

}

