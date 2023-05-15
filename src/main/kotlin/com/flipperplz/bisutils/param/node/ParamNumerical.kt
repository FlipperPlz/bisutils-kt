package com.flipperplz.bisutils.param.node

interface ParamNumerical : ParamLiteral<Number> {
    override fun toParam(): String = slimValue.toString()
}