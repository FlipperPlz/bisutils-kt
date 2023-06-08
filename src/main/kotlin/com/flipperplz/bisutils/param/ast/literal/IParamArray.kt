package com.flipperplz.bisutils.param.ast.literal

import com.flipperplz.bisutils.family.IFamilyMember
import com.flipperplz.bisutils.family.IFamilyParent
import com.flipperplz.bisutils.options.IOptions
import com.flipperplz.bisutils.param.ast.node.IParamLiteral
import com.flipperplz.bisutils.param.ast.node.IParamLiteralBase

interface IParamArray : IParamLiteral<List<IParamLiteralBase>>, IFamilyParent {
    companion object;
    override fun isBinarizable(): Boolean = slimValue?.all { it.isBinarizable() } ?: true
    override val children: List<IParamLiteralBase>?
    override val slimValue: List<IParamLiteralBase>? get() = children

    override fun isValid(options: IOptions?): Boolean = super.isValid(options) && slimValue?.all { it.isValid(options) } ?: false

    override fun toParam(): String =
        (slimValue ?: emptyList()).joinToString(", ", prefix = "{", postfix = "}") { it.toParam() }
}