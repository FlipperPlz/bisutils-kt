package com.flipperplz.bisutils.param.literal

import com.flipperplz.bisutils.param.node.RapNumerical
import com.flipperplz.bisutils.param.utils.ParamElementTypes

interface RapFloat : RapNumerical {
    override fun getRapElementType(): ParamElementTypes =
        ParamElementTypes.L_FLOAT

    override fun isBinarizable(): Boolean =
        true

    override val slimValue: Float?
}