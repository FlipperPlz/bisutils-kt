package com.flipperplz.bisutils.param.literal

import com.flipperplz.bisutils.param.node.RapLiteral
import com.flipperplz.bisutils.param.utils.ParamLiteralTypes

interface RapInt : RapLiteral<Int> {
    override val literalId: Byte
        get() = 2
    override val slimLiteralType: ParamLiteralTypes
        get() = ParamLiteralTypes.INTEGER

    override fun toEnforce(): String = slimValue.toString()
}