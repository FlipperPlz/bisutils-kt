package com.flipperplz.bisutils.param.slim.structure.commands

import com.flipperplz.bisutils.param.slim.structure.ParamSlimCommand

interface ParamSlimClass : ParamSlimExternalClass {
    val slimSuperClass: String?
    val slimCommands: List<ParamSlimCommand>

    override fun currentlyValid(): Boolean = super.currentlyValid() &&
            slimCommands.all { it.currentlyValid() }

    override fun toEnforce(): String {
        val builder = StringBuilder(super.toEnforce())
        if(slimSuperClass != null) builder.append(" : ").append(slimSuperClass)
        builder.append(" { \n")
        builder.append(slimCommands.joinToString(separator = "\n", postfix = "\n"){ it.toEnforce() })
        return builder.append("};").toString()
    }
}