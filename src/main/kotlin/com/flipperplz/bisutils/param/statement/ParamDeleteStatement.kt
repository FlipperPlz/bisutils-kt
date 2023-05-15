package com.flipperplz.bisutils.param.statement

import com.flipperplz.bisutils.param.node.ParamNamedElement
import com.flipperplz.bisutils.param.node.ParamStatement
import com.flipperplz.bisutils.param.utils.ParamElementTypes

interface ParamDeleteStatement : ParamStatement, ParamNamedElement {
    companion object;
    override val slimName: String?

    override fun getParamElementType(): ParamElementTypes = ParamElementTypes.C_DELETE

    fun shouldValidateTarget(): Boolean

    fun locateTargetClass(): ParamExternalClass?

    override fun isCurrentlyValid(): Boolean =
        !slimName.isNullOrBlank() && (shouldValidateTarget() && locateTargetClass() != null)

    override fun isBinarizable(): Boolean = true

    override fun toParam(): String = "delete $slimName;"
}