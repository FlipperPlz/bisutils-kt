package com.flipperplz.bisutils.param.slim.literals

import com.flipperplz.bisutils.param.slim.node.ParamSlimLiteral
import com.flipperplz.bisutils.param.slim.util.ParamLiteralTypes

interface ParamSlimString : ParamSlimLiteral<String>, CharSequence {
    override val slimLiteralType: ParamLiteralTypes
        get() = ParamLiteralTypes.STRING

    override val length: Int
        get() = slimValue?.length ?: 0

    override operator fun get(index: Int): Char =
        slimValue?.get(index) ?: 0x0.toChar()

    override fun subSequence(startIndex: Int, endIndex: Int): CharSequence =
        slimValue?.subSequence(startIndex, endIndex) ?: ""

    override fun toEnforce(): String = if(slimValue != null) "\"${slimValue!!.replace("\"", "\"\"")}\"" else "//Unknown String"
}