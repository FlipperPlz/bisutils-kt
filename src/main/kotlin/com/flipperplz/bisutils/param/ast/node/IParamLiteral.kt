package com.flipperplz.bisutils.param.ast.node

interface IParamLiteral<out T> : IParamLiteralBase {
    override val slimValue: T?
}