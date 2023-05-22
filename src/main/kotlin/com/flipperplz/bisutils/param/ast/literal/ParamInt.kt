package com.flipperplz.bisutils.param.ast.literal

import com.flipperplz.bisutils.param.ast.node.ParamNumerical
import com.flipperplz.bisutils.param.utils.ParamElementTypes

interface ParamInt : ParamNumerical {
    companion object;

    override fun getParamElementType(): ParamElementTypes = ParamElementTypes.L_INT
    override fun isBinarizable(): Boolean = true

    override val slimValue: Int?
}