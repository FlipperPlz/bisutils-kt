package com.flipperplz.bisutils.param.literal

import com.flipperplz.bisutils.param.node.ParamLiteral
import com.flipperplz.bisutils.param.node.ParamLiteralBase
import com.flipperplz.bisutils.param.utils.ParamElementTypes

interface ParamArray : ParamLiteral<List<ParamLiteralBase>> {
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