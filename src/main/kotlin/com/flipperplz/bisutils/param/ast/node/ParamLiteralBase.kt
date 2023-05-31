package com.flipperplz.bisutils.param.ast.node

interface ParamLiteralBase : ParamElement {
    companion object

    val slimValue: Any?

    override fun isCurrentlyValid(): Boolean = slimValue != null
}