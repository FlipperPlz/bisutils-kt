package com.flipperplz.bisutils.param.statement

import com.flipperplz.bisutils.param.literal.RapArray
import com.flipperplz.bisutils.param.node.RapLiteralBase
import com.flipperplz.bisutils.param.node.RapNamedElement
import com.flipperplz.bisutils.param.node.RapStatement
import com.flipperplz.bisutils.param.utils.ParamCommandTypes
import com.flipperplz.bisutils.param.utils.ParamOperatorTypes

interface RapVariableStatement : RapStatement, RapNamedElement {
    val slimValue: RapLiteralBase?
    val slimOperator: ParamOperatorTypes?

    override val slimCommandType: ParamCommandTypes
        get() = if(slimValue is RapArray || (slimOperator != ParamOperatorTypes.ASSIGN || slimOperator != null))
            ParamCommandTypes.ARRAY else
            ParamCommandTypes.VARIABLE

    override val statementId: Byte
        get() = 1

    override val slimCurrentlyValid: Boolean
        get() = !slimName.isNullOrBlank() && slimValue != null && (slimOperator == ParamOperatorTypes.ASSIGN || slimValue is RapArray)

    override fun toEnforce(): String {
        val builder = StringBuilder(slimName)
        if (slimValue is RapArray) builder.append("[]")
        builder.append(' ').append(slimOperator?.text).append(' ')
        return builder.append(slimValue?.toEnforce()).append(';').toString()
    }
}