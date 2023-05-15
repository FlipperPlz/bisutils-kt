package com.flipperplz.bisutils.param.literal

import com.flipperplz.bisutils.param.node.ParamNumerical
import com.flipperplz.bisutils.param.utils.ParamElementTypes

interface ParamInt : ParamNumerical {
    companion object;

    override fun getParamElementType(): ParamElementTypes = ParamElementTypes.L_INT
    override fun isBinarizable(): Boolean = true

    override val slimValue: Int?
}