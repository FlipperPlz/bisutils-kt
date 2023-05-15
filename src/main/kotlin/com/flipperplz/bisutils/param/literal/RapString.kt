package com.flipperplz.bisutils.param.literal

import com.flipperplz.bisutils.param.node.RapLiteral
import com.flipperplz.bisutils.param.utils.ParamElementTypes
import com.flipperplz.bisutils.param.utils.ParamStringType

interface RapString : RapLiteral<String> {
    companion object;

    val slimStringType: ParamStringType

    override fun isBinarizable(): Boolean =
        true

    override fun getRapElementType(): ParamElementTypes =
        ParamElementTypes.L_STRING

    override fun isCurrentlyValid(): Boolean =
        super.isCurrentlyValid() &&
        (slimValue?.length ?: 1025) <= 1024

    override fun toParam(): String =
        slimStringType.stringify(slimValue ?: "")

}