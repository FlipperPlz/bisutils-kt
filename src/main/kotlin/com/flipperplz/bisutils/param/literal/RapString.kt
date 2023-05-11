package com.flipperplz.bisutils.param.literal

import com.flipperplz.bisutils.param.node.RapLiteral
import com.flipperplz.bisutils.param.utils.ParamLiteralTypes

interface RapString : RapLiteral<String> {
    override val literalId: Byte?
        get() = 0

    override val slimLiteralType: ParamLiteralTypes
        get() = ParamLiteralTypes.STRING

    override fun toParam(): String =
        if (slimValue != null) "\"${slimValue!!.replace("\"", "\"\"")}\"" else "//Unknown String"
}