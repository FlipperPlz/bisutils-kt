package com.flipperplz.bisutils.param.slim.literals

import com.flipperplz.bisutils.param.slim.node.ParamSlimLiteral
import com.flipperplz.bisutils.param.slim.util.ParamLiteralTypes

interface ParamSlimArray : ParamSlimLiteral<List<ParamSlimLiteral<*>>> {
    override val slimLiteralType: ParamLiteralTypes
        get() = ParamLiteralTypes.ARRAY

    override val slimCurrentlyValid: Boolean
        get() = super.slimCurrentlyValid && slimValue?.all { it.slimCurrentlyValid } ?: false
    override fun toEnforce(): String = (slimValue ?: emptyList()).joinToString(", ", prefix = "{", postfix = "}") { it.toEnforce() }
}