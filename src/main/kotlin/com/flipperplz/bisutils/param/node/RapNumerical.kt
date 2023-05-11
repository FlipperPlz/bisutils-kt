package com.flipperplz.bisutils.param.node

interface RapNumerical : RapLiteral<Number> {
    override fun toParam(): String = slimValue.toString()
}