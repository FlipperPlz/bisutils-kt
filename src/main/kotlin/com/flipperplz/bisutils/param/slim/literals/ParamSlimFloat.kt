package com.flipperplz.bisutils.param.slim.literals

import com.flipperplz.bisutils.param.slim.ParamSlimLiteral
import com.flipperplz.bisutils.param.slim.util.ParamLiteralTypes

interface ParamSlimFloat : ParamSlimLiteral<Float> {
    override val slimLiteralType: ParamLiteralTypes
        get() = ParamLiteralTypes.FLOAT
    override fun toEnforce(): String = slimValue.toString()
}
