package com.flipperplz.bisutils.param.statement

import com.flipperplz.bisutils.param.literal.RapArray
import com.flipperplz.bisutils.param.node.RapLiteralBase
import com.flipperplz.bisutils.param.node.RapNamedElement
import com.flipperplz.bisutils.param.node.RapStatement
import com.flipperplz.bisutils.param.utils.ParamElementTypes
import com.flipperplz.bisutils.param.utils.ParamOperatorTypes

interface RapVariableStatement : RapStatement, RapNamedElement {
    val slimValue: RapLiteralBase?
    val slimOperator: ParamOperatorTypes?

    override fun getRapElementType(): ParamElementTypes = when {
        slimValue is RapArray -> if(slimOperator != ParamOperatorTypes.ASSIGN)
            ParamElementTypes.C_VARIABLE_ARRAY_FLAGGED else
            ParamElementTypes.C_VARIABLE_ARRAY
        slimOperator != ParamOperatorTypes.ASSIGN ->  ParamElementTypes.C_VARIABLE_ARRAY_FLAGGED
        else -> ParamElementTypes.C_VARIABLE
    }

    override fun isCurrentlyValid(): Boolean =
        !slimName.isNullOrBlank() &&
        slimValue?.isCurrentlyValid() ?: false &&
        slimOperator != null &&
        (slimOperator == ParamOperatorTypes.ASSIGN || slimValue is RapArray)

    override fun isBinarizable(): Boolean =
        slimValue?.isBinarizable() ?: false

    override fun toParam(): String {
        val builder = StringBuilder(slimName)
        if (slimValue is RapArray) builder.append("[]")
        builder.append(' ').append(slimOperator?.text).append(' ')
        return builder.append(slimValue?.toParam()).append(';').toString()
    }
}