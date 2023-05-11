package com.flipperplz.bisutils.param.literal

import com.flipperplz.bisutils.param.node.RapLiteral

interface RapNumerical : RapLiteral<Number> {

    override fun toParam(): String = slimValue.toString()
}