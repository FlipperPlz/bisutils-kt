package com.flipperplz.bisutils.param.literal

import com.flipperplz.bisutils.param.node.ParamNumerical
import com.flipperplz.bisutils.param.utils.ParamElementTypes

interface ParamFloat : ParamNumerical {
    companion object;

    override fun getParamElementType(): ParamElementTypes =
        ParamElementTypes.L_FLOAT

    override fun isBinarizable(): Boolean =
        true

    override val slimValue: Float?
}