package com.flipperplz.bisutils.param.ast.misc

import com.flipperplz.bisutils.options.IOptions
import com.flipperplz.bisutils.param.ast.node.IParamElement
import com.flipperplz.bisutils.param.ast.node.IParamElement.Companion.REGEX_ALPHANUM
import com.flipperplz.bisutils.param.ast.node.IParamLiteral
import com.flipperplz.bisutils.param.ast.node.IParamLiteralParent
import com.flipperplz.bisutils.param.ast.node.IParamNumerical
import com.flipperplz.bisutils.param.ast.statement.IParamEnum

interface IParamEnumValue : IParamElement, IParamLiteralParent {
    override val parent: IParamEnum?
    val paramName: String?
    val paramValue: IParamNumerical?
    override val children: List<IParamLiteral>?
        get() = paramValue?.let { listOf(it) } ?: emptyList()

    override fun isValid(options: IOptions?): Boolean = !paramName.isNullOrBlank() && REGEX_ALPHANUM.matches(paramName!!)
    override fun toParam(): String = paramName?.let {
        paramValue?.let { return "$paramName=${paramValue!!.toParam()}" }
        return it
    } ?: throw Exception() //TODO: Param Serialization Exception (isValid was false)

}