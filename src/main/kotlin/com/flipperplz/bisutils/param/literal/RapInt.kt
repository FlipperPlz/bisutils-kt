package com.flipperplz.bisutils.param.literal

import com.flipperplz.bisutils.param.node.RapNumerical
import com.flipperplz.bisutils.param.utils.ParamElementTypes

interface RapInt : RapNumerical {
    override fun getRapElementType(): ParamElementTypes =
        ParamElementTypes.L_INT

    override fun isBinarizable(): Boolean =
        true

    override val slimValue: Int?
}