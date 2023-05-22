package com.flipperplz.bisutils.param.ast.node

interface ParamLiteral<T> : ParamLiteralBase {
    override val slimValue: T?
}