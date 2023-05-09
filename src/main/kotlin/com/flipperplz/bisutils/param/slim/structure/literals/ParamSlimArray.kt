package com.flipperplz.bisutils.param.slim.structure.literals

import com.flipperplz.bisutils.param.slim.structure.ParamSlimLiteral
import com.flipperplz.bisutils.param.slim.util.ParamLiteralTypes

interface ParamSlimArray : ParamSlimLiteral<List<ParamSlimLiteral<*>>> {
    override val literalType: ParamLiteralTypes
        get() = ParamLiteralTypes.ARRAY

    override fun currentlyValid(): Boolean = value?.all { it.currentlyValid() } ?: false
    override fun toEnforce(): String = (value ?: emptyList()).joinToString(", ", prefix = "{", postfix = "}") { it.toEnforce() }
}