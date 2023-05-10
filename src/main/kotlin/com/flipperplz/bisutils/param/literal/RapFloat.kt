package com.flipperplz.bisutils.param.literal

import com.flipperplz.bisutils.param.node.RapLiteral
import com.flipperplz.bisutils.param.utils.ParamLiteralTypes

interface RapFloat : RapLiteral<Float> {
    override val literalId: Byte
        get() = 1
    override val slimLiteralType: ParamLiteralTypes
        get() = ParamLiteralTypes.FLOAT

    override fun toEnforce(): String = slimValue.toString()
}