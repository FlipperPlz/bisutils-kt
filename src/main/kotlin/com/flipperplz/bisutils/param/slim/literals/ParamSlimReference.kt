package com.flipperplz.bisutils.param.slim.literals

import com.flipperplz.bisutils.param.slim.node.ParamSlimLiteral
import com.flipperplz.bisutils.param.slim.util.ParamLiteralTypes

interface ParamSlimReference : ParamSlimLiteral<String> {
    override val slimLiteralType: ParamLiteralTypes
        get() = ParamLiteralTypes.REFERENCE

    override fun toEnforce(): String = "@$slimValue"
}