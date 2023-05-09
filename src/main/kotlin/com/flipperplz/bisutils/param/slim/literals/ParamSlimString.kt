package com.flipperplz.bisutils.param.slim.literals

import com.flipperplz.bisutils.param.slim.node.ParamSlimLiteral
import com.flipperplz.bisutils.param.slim.util.ParamLiteralTypes

interface ParamSlimString : ParamSlimLiteral<String> {
    override val slimLiteralType: ParamLiteralTypes
        get() = ParamLiteralTypes.STRING

    override fun toEnforce(): String = if(slimValue != null) "\"${slimValue!!.replace("\"", "\"\"")}\"" else "//Unknown String"
}