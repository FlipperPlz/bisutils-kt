package com.flipperplz.bisutils.options.param.ast.node

interface ParamNumerical : ParamLiteral<Number> {
    override fun toParam(): String = slimValue.toString()
}