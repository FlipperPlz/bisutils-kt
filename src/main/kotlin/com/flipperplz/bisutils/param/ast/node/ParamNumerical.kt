package com.flipperplz.bisutils.param.ast.node

interface ParamNumerical : ParamLiteral<Number> {
    override fun toParam(): String = slimValue.toString()
}