package com.flipperplz.bisutils.param.literal

import com.flipperplz.bisutils.param.node.RapLiteral
import com.flipperplz.bisutils.param.utils.ParamLiteralTypes

interface RapString : RapLiteral<String>, CharSequence {
    override val literalId: Byte?
        get() = 0

    override val slimLiteralType: ParamLiteralTypes
        get() = ParamLiteralTypes.STRING

    override val length: Int
        get() = slimValue?.length ?: 0

    override operator fun get(index: Int): Char =
        slimValue?.get(index) ?: 0x0.toChar()

    override fun subSequence(startIndex: Int, endIndex: Int): CharSequence =
        slimValue?.subSequence(startIndex, endIndex) ?: ""

    override fun toEnforce(): String =
        if (slimValue != null) "\"${slimValue!!.replace("\"", "\"\"")}\"" else "//Unknown String"
}