package com.flipperplz.bisutils.param.ast.node

interface IParamNumerical : IParamLiteral<Number> {
    override fun toParam(): String = slimValue.toString()
}