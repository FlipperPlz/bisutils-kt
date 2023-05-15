package com.flipperplz.bisutils.param.literal

import com.flipperplz.bisutils.param.node.RapLiteral
import com.flipperplz.bisutils.param.node.RapLiteralBase
import com.flipperplz.bisutils.param.utils.ParamElementTypes

interface RapArray : RapLiteral<List<RapLiteralBase>> {
    companion object;
    override fun isBinarizable(): Boolean =
        slimValue?.all { it.isBinarizable() } ?: true

    override fun isCurrentlyValid(): Boolean =
        super.isCurrentlyValid() && slimValue?.all { it.isCurrentlyValid() } ?: false

    override fun getRapElementType(): ParamElementTypes =
        ParamElementTypes.L_ARRAY

    override fun toParam(): String =
        (slimValue ?: emptyList()).joinToString(", ", prefix = "{", postfix = "}") { it.toParam() }
}