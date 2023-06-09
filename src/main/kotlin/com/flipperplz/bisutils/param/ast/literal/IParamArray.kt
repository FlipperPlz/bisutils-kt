package com.flipperplz.bisutils.param.ast.literal

import com.flipperplz.bisutils.options.IOptions
import com.flipperplz.bisutils.param.ast.node.IParamLiteralParent
import com.flipperplz.bisutils.param.ast.node.IParamLiteral

interface IParamArray : IParamLiteral, IParamLiteralParent {
    companion object;
    override val children: List<IParamLiteral>? get() = paramValue
    override val parent: IParamLiteralParent?
    override val paramValue: List<IParamLiteral>?
    override fun isValid(options: IOptions?): Boolean = super.isValid(options) && paramValue?.all { it.isValid(options) } ?: false

    override fun toParam(): String =
        (paramValue ?: emptyList()).joinToString(", ", prefix = "{", postfix = "}") { it.toParam() }
}