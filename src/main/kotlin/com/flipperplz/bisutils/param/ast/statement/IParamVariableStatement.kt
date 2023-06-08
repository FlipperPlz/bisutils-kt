package com.flipperplz.bisutils.param.ast.statement

import com.flipperplz.bisutils.options.IOptions
import com.flipperplz.bisutils.param.ast.literal.IParamArray
import com.flipperplz.bisutils.param.ast.node.IParamLiteralBase
import com.flipperplz.bisutils.param.ast.node.IParamNamedElement
import com.flipperplz.bisutils.param.ast.node.IParamStatement
import com.flipperplz.bisutils.param.utils.ParamOperatorTypes

interface IParamVariableStatement : IParamStatement, IParamNamedElement {
    companion object

    val slimValue: IParamLiteralBase?
    val slimOperator: ParamOperatorTypes?
    override fun isValid(options: IOptions?): Boolean =
        !slimName.isNullOrBlank() &&
        slimValue?.isValid(options) ?: false &&
        slimOperator != null &&
        (slimOperator == ParamOperatorTypes.ASSIGN || slimValue is IParamArray)


    override fun isBinarizable(): Boolean =
        /*isCurrentlyValid() &&*/ slimValue?.isBinarizable() ?: false

    override fun toParam(): String {
        val builder = StringBuilder(slimName)
        if (slimValue is IParamArray) builder.append("[]")
        builder.append(' ').append(slimOperator?.text).append(' ')
        return builder.append(slimValue?.toParam()).append(';').toString()
    }
}