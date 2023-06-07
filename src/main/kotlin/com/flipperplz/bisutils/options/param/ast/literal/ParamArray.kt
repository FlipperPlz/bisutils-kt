package com.flipperplz.bisutils.options.param.ast.literal

import com.flipperplz.bisutils.options.param.ast.node.ParamLiteral
import com.flipperplz.bisutils.options.param.ast.node.ParamLiteralBase
import com.flipperplz.bisutils.options.param.utils.ParamElementTypes

interface ParamArray : ParamLiteral<List<ParamLiteralBase>>, List<ParamLiteralBase> {
    companion object;
    override fun isBinarizable(): Boolean =
        slimValue?.all { it.isBinarizable() } ?: true

    override fun isCurrentlyValid(): Boolean =
        super.isCurrentlyValid() && slimValue?.all { it.isCurrentlyValid() } ?: false

    override fun getParamElementType(): ParamElementTypes =
        ParamElementTypes.L_ARRAY

    override fun toParam(): String =
        (slimValue ?: emptyList()).joinToString(", ", prefix = "{", postfix = "}") { it.toParam() }
}