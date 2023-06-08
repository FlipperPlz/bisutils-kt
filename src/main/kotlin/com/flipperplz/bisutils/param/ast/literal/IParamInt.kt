package com.flipperplz.bisutils.param.ast.literal

import com.flipperplz.bisutils.param.ast.node.IParamNumerical

interface IParamInt : IParamNumerical {
    companion object;

    override fun isBinarizable(): Boolean = true

    override val slimValue: Int?
}