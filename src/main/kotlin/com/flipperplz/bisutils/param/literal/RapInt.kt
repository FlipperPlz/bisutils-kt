package com.flipperplz.bisutils.param.literal

import com.flipperplz.bisutils.param.node.RapLiteral
import com.flipperplz.bisutils.param.utils.ParamLiteralTypes

interface RapInt : RapNumerical {
    override val literalId: Byte
        get() = 2
    override val slimLiteralType: ParamLiteralTypes
        get() = ParamLiteralTypes.INTEGER
    override val slimValue: Int?

    override fun toParam(): String = slimValue.toString()
}