package com.flipperplz.bisutils.param.slim.literals

import com.flipperplz.bisutils.param.slim.ParamSlimLiteral
import com.flipperplz.bisutils.param.slim.util.ParamLiteralTypes

interface ParamSlimInt : ParamSlimLiteral<Int> {
    override val slimLiteralType: ParamLiteralTypes
        get() = ParamLiteralTypes.INTEGER
    override fun toEnforce(): String = slimValue.toString()
}
