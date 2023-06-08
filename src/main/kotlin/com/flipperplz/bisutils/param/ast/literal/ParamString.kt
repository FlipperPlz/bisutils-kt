package com.flipperplz.bisutils.param.ast.literal

import com.flipperplz.bisutils.param.ast.node.ParamLiteral
import com.flipperplz.bisutils.param.utils.ParamElementTypes
import com.flipperplz.bisutils.param.utils.ParamStringType

interface ParamString : ParamLiteral<String>, CharSequence {
    companion object

    val slimStringType: ParamStringType

    override fun isBinarizable(): Boolean =
        true

    override fun getParamElementType(): ParamElementTypes =
        ParamElementTypes.L_STRING

    override fun isCurrentlyValid(): Boolean =
        super.isCurrentlyValid() &&
        (slimValue?.length ?: 1025) <= 1024

    override fun toParam(): String =
        slimStringType.stringify(slimValue ?: "")

}