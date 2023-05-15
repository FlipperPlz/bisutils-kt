package com.flipperplz.bisutils.param.node

interface ParamLiteralBase : ParamElement {
    companion object;
    val slimValue: Any?

    override fun isCurrentlyValid(): Boolean = slimValue != null
}