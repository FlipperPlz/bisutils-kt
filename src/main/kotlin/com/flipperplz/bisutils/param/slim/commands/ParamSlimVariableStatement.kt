package com.flipperplz.bisutils.param.slim.commands

import com.flipperplz.bisutils.param.slim.node.ParamSlimCommand
import com.flipperplz.bisutils.param.slim.node.ParamSlimLiteral
import com.flipperplz.bisutils.param.slim.literals.ParamSlimArray
import com.flipperplz.bisutils.param.slim.util.ParamCommandTypes
import com.flipperplz.bisutils.param.slim.util.ParamOperatorTypes

interface ParamSlimVariableStatement : ParamSlimCommand {
    val slimName: String?;
    val slimValue: ParamSlimLiteral<*>?
    val slimOperator: ParamOperatorTypes?
    override val slimCommandType: ParamCommandTypes
        get() = if(slimValue is ParamSlimArray || (slimOperator != ParamOperatorTypes.ASSIGN || slimOperator != null))
            ParamCommandTypes.ARRAY else
            ParamCommandTypes.VARIABLE

    override val slimCurrentlyValid: Boolean
        get() = !slimName.isNullOrBlank() && slimValue != null && (slimOperator == ParamOperatorTypes.ASSIGN || slimValue is ParamSlimArray)

    override fun toEnforce(): String {
        val builder = StringBuilder(slimName)
        if (slimValue is ParamSlimArray) builder.append("[]")
        builder.append(' ').append(slimOperator?.text).append(' ')
        return builder.append(slimValue?.toEnforce()).append(';').toString()
    }
}

