package com.flipperplz.bisutils.param.node

interface ParamLiteral<T> : ParamLiteralBase {
    override val slimValue: T?
}