package com.flipperplz.bisutils.options.param.ast.node

interface ParamLiteral<T> : ParamLiteralBase {
    override val slimValue: T?
}