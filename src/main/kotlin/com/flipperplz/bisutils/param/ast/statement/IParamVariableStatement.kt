package com.flipperplz.bisutils.param.ast.statement

import com.flipperplz.bisutils.options.IOptions
import com.flipperplz.bisutils.param.ast.literal.IParamArray
import com.flipperplz.bisutils.param.ast.node.IParamElement
import com.flipperplz.bisutils.param.ast.node.IParamLiteral
import com.flipperplz.bisutils.param.ast.node.IParamLiteralParent
import com.flipperplz.bisutils.param.ast.node.IParamStatement
import com.flipperplz.bisutils.param.utils.ParamOperatorTypes

interface IParamVariableStatement : IParamStatement, IParamLiteralParent {
    companion object
    val paramName: String?
    val paramValue: IParamLiteral?
    val paramOperator: ParamOperatorTypes?
    override val children: List<IParamLiteral>?
        get() = paramValue?.let { listOf(it) } ?: emptyList()

    override fun isValid(options: IOptions?): Boolean =
        !paramName.isNullOrBlank() &&
        IParamElement.REGEX_ALPHANUM.matches(paramName!!) &&
        paramValue?.isValid(options) ?: false &&
        paramOperator != null &&
        (paramOperator == ParamOperatorTypes.ASSIGN || paramValue is IParamArray)

    override fun toParam(): String {
        val builder = StringBuilder(paramName)
        if (paramValue is IParamArray) builder.append("[]")
        builder.append(' ').append(paramOperator?.text).append(' ')
        return builder.append(paramValue?.toParam()).append(';').toString()
    }
}