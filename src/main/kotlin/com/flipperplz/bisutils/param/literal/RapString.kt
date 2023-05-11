package com.flipperplz.bisutils.param.literal

import com.flipperplz.bisutils.param.directive.RapInclude
import com.flipperplz.bisutils.param.node.RapLiteral
import com.flipperplz.bisutils.param.utils.ParamElementTypes
import com.flipperplz.bisutils.param.utils.ParamStringType

interface RapString : RapLiteral<String> {
    val slimStringType: ParamStringType

    override fun isBinarizable(): Boolean =
        slimStringType != ParamStringType.ANGLE

    override fun getRapElementType(): ParamElementTypes =
        ParamElementTypes.L_STRING

    override fun isCurrentlyValid(): Boolean =
        super.isCurrentlyValid() &&
        (slimStringType != ParamStringType.ANGLE || slimParent is RapInclude) &&
        (slimValue?.length ?: 1025) <= 1024

    override fun toParam(): String =
        slimStringType.stringify(slimValue ?: "")
}