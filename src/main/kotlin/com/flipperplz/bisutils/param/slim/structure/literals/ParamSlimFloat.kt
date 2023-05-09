package com.flipperplz.bisutils.param.slim.structure.literals

import com.flipperplz.bisutils.param.slim.structure.ParamSlimLiteral
import com.flipperplz.bisutils.param.slim.util.ParamLiteralTypes

interface ParamSlimFloat : ParamSlimLiteral<Float> {
    override val literalType: ParamLiteralTypes
        get() = ParamLiteralTypes.FLOAT
    override fun toEnforce(): String = value.toString()
}
