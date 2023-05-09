package com.flipperplz.bisutils.param.slim.structure.literals

import com.flipperplz.bisutils.param.slim.structure.ParamSlimLiteral
import com.flipperplz.bisutils.param.slim.util.ParamLiteralTypes

interface ParamSlimString : ParamSlimLiteral<String> {
    override val literalType: ParamLiteralTypes
        get() = ParamLiteralTypes.STRING
    override fun toEnforce(): String = if(value != null) "\"${value!!.replace("\"", "\"\"")}\"" else "//Unknown String"
}