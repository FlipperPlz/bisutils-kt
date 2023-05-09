package com.flipperplz.bisutils.param.slim

import com.flipperplz.bisutils.param.slim.structure.ParamSlimCommand
import com.flipperplz.bisutils.param.slim.structure.ParamSlimLiteral
import com.flipperplz.bisutils.param.slim.structure.literals.ParamSlimArray
import com.flipperplz.bisutils.param.slim.util.ParamLiteralTypes
import com.flipperplz.bisutils.param.slim.util.ParamOperatorTypes

interface ParamSlim {
    fun toEnforce(): String = "//Unknown"
    fun currentlyValid(): Boolean = true
}


interface ParamSlimExternalClass : ParamSlimCommand {
    var className: String
    override fun toEnforce(): String = "class $className;"
}

interface ParamSlimClass : ParamSlimExternalClass {
    var superClass: String?
    var slimCommands: List<ParamSlimCommand>

    override fun toEnforce(): String {
        val builder = StringBuilder("class ").append(className)
        if(superClass != null) builder.append(" : ").append(superClass)
        builder.append(" { \n")
        builder.append(slimCommands.joinToString(separator = "\n", postfix = "\n"){ it.toEnforce() })
        return builder.append("};").toString()
    }
}

interface ParamSlimDeleteStatement : ParamSlimCommand {
    var target: String

    override fun toEnforce(): String = "delete $target;"
}

interface ParamSlimVariableStatement : ParamSlimCommand {
    var name: String;
    var value: ParamSlimLiteral<*>
    var operator: ParamOperatorTypes
    override fun toEnforce(): String {
        val builder = StringBuilder(name)
        if (value is ParamSlimArray) builder.append("[]")
        builder.append(' ').append(operator.text).append(' ')
        return builder.append(value.toEnforce()).append(';').toString()
    }
}



