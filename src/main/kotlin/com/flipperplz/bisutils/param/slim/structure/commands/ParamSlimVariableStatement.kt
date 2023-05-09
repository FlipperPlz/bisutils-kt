package com.flipperplz.bisutils.param.slim.structure.commands

import com.flipperplz.bisutils.param.slim.structure.ParamSlimCommand
import com.flipperplz.bisutils.param.slim.structure.ParamSlimLiteral
import com.flipperplz.bisutils.param.slim.structure.literals.ParamSlimArray
import com.flipperplz.bisutils.param.slim.util.ParamOperatorTypes

interface ParamSlimVariableStatement : ParamSlimCommand {
    val slimName: String?;
    val slimValue: ParamSlimLiteral<*>?
    val slimOperator: ParamOperatorTypes?

    override fun currentlyValid(): Boolean {
        if(!slimName.isNullOrEmpty() && slimValue != null && slimOperator != null) {
            if(slimOperator != ParamOperatorTypes.ASSIGN && slimValue !is ParamSlimArray) return false
            return true
        }
        return false
    }

    override fun toEnforce(): String {
        val builder = StringBuilder(slimName)
        if (slimValue is ParamSlimArray) builder.append("[]")
        builder.append(' ').append(slimOperator?.text).append(' ')
        return builder.append(slimValue?.toEnforce()).append(';').toString()
    }
}

