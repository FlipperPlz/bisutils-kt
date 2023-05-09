package com.flipperplz.bisutils.param.slim.commands

import com.flipperplz.bisutils.param.slim.ParamSlimCommand
import com.flipperplz.bisutils.param.slim.util.ParamCommandTypes

interface ParamSlimClass : ParamSlimExternalClass {
    val slimSuperClass: String?
    val slimCommands: List<ParamSlimCommand>
    override val slimCommandType: ParamCommandTypes
        get() = ParamCommandTypes.CLASS

    override val slimCurrentlyValid: Boolean
        get() = super.slimCurrentlyValid && slimCommands.all { it.slimCurrentlyValid }


    override fun toEnforce(): String {
        val builder = StringBuilder(super.toEnforce())
        if(slimSuperClass != null) builder.append(" : ").append(slimSuperClass)
        builder.append(" { \n")
        builder.append(slimCommands.joinToString(separator = "\n", postfix = "\n"){ it.toEnforce() })
        return builder.append("};").toString()
    }
}