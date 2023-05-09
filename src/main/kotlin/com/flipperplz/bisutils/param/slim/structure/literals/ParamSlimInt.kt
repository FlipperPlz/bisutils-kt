package com.flipperplz.bisutils.param.slim.structure.literals

import com.flipperplz.bisutils.param.slim.structure.ParamSlimLiteral
import com.flipperplz.bisutils.param.slim.util.ParamLiteralTypes

interface ParamSlimInt : ParamSlimLiteral<Int> {
    override val literalType: ParamLiteralTypes
        get() = ParamLiteralTypes.INTEGER
    override fun toEnforce(): String = value.toString()
}
