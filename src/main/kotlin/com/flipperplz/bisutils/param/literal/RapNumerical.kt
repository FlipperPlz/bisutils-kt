package com.flipperplz.bisutils.param.literal

import com.flipperplz.bisutils.param.node.RapLiteral

interface RapNumerical : RapLiteral<Number> {

    override fun toEnforce(): String = slimValue.toString()
}