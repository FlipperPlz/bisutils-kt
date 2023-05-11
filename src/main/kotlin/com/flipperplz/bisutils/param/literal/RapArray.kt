package com.flipperplz.bisutils.param.literal

import com.flipperplz.bisutils.param.node.RapLiteral
import com.flipperplz.bisutils.param.node.RapLiteralBase
import com.flipperplz.bisutils.param.utils.ParamLiteralTypes

interface RapArray : RapLiteral<List<RapLiteralBase>> {
    override val literalId: Byte
        get() = 3

    override val slimLiteralType: ParamLiteralTypes
        get() = ParamLiteralTypes.ARRAY

    override val slimCurrentlyValid: Boolean
        get() = super.slimCurrentlyValid && slimValue?.all { it.slimCurrentlyValid } ?: false

    override fun toParam(): String =
        (slimValue ?: emptyList()).joinToString(", ", prefix = "{", postfix = "}") { it.toParam() }
}