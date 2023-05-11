package com.flipperplz.bisutils.param.literal

import com.flipperplz.bisutils.param.node.RapLiteral
import com.flipperplz.bisutils.param.utils.ParamLiteralTypes

interface RapReference : RapLiteral<String> {
    override val slimBinarizable: Boolean
        get() = false
    override val literalId: Byte?
        get() = null
    override val slimLiteralType: ParamLiteralTypes
        get() = ParamLiteralTypes.REFERENCE

    override fun toParam(): String = "@$slimValue"
}