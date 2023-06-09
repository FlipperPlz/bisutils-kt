package com.flipperplz.bisutils.param.ast.node

interface IParamNumerical : IParamLiteral {
    override val paramValue: Number?
    override fun toParam(): String = paramValue.toString()
}