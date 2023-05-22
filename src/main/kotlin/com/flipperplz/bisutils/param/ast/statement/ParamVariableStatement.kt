package com.flipperplz.bisutils.param.ast.statement

import com.flipperplz.bisutils.param.ast.literal.ParamArray
import com.flipperplz.bisutils.param.ast.node.ParamLiteralBase
import com.flipperplz.bisutils.param.ast.node.ParamNamedElement
import com.flipperplz.bisutils.param.ast.node.ParamStatement
import com.flipperplz.bisutils.param.utils.ParamElementTypes
import com.flipperplz.bisutils.param.utils.ParamOperatorTypes

interface ParamVariableStatement : ParamStatement, ParamNamedElement {
    companion object;
    val slimValue: ParamLiteralBase?
    val slimOperator: ParamOperatorTypes?

    override fun getParamElementType(): ParamElementTypes = when {
        slimValue is ParamArray -> if(slimOperator != ParamOperatorTypes.ASSIGN)
            ParamElementTypes.C_VARIABLE_ARRAY_FLAGGED else
            ParamElementTypes.C_VARIABLE_ARRAY
        slimOperator != ParamOperatorTypes.ASSIGN ->  ParamElementTypes.C_VARIABLE_ARRAY_FLAGGED
        else -> ParamElementTypes.C_VARIABLE
    }

    override fun isCurrentlyValid(): Boolean =
        !slimName.isNullOrBlank() &&
        slimValue?.isCurrentlyValid() ?: false &&
        slimOperator != null &&
        (slimOperator == ParamOperatorTypes.ASSIGN || slimValue is ParamArray)

    override fun isBinarizable(): Boolean =
        isCurrentlyValid() && slimValue?.isBinarizable() ?: false

    override fun toParam(): String {
        val builder = StringBuilder(slimName)
        if (slimValue is ParamArray) builder.append("[]")
        builder.append(' ').append(slimOperator?.text).append(' ')
        return builder.append(slimValue?.toParam()).append(';').toString()
    }
}