package com.flipperplz.bisutils.param.literal

import com.flipperplz.bisutils.param.node.RapLiteral
import com.flipperplz.bisutils.param.statement.RapVariableStatement
import com.flipperplz.bisutils.param.utils.ParamElementTypes
import com.flipperplz.bisutils.param.utils.ParamStringType

interface RapReference : RapLiteral<String> {
    val slimStringType: ParamStringType

    fun locateReference(): RapVariableStatement?

    fun shouldValidateReference(): Boolean

    override fun isBinarizable(): Boolean =
        false

    override fun getRapElementType(): ParamElementTypes =
        ParamElementTypes.L_REFERENCE

    override fun isCurrentlyValid(): Boolean =
        super.isCurrentlyValid() &&
        (shouldValidateReference() && locateReference() != null) &&
        slimStringType != ParamStringType.ANGLE

    override fun toParam(): String =
        "@${slimStringType.stringify(slimValue ?: "")}"
}