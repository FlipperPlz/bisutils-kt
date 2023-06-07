package com.flipperplz.bisutils.options.param.ast.literal

import com.flipperplz.bisutils.options.param.ast.node.ParamNumerical
import com.flipperplz.bisutils.options.param.utils.ParamElementTypes

interface ParamFloat : ParamNumerical {
    companion object;

    override fun getParamElementType(): ParamElementTypes =
        ParamElementTypes.L_FLOAT

    override fun isBinarizable(): Boolean =
        true

    override val slimValue: Float?
}