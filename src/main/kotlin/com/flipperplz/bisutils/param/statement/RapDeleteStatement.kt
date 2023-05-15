package com.flipperplz.bisutils.param.statement

import com.flipperplz.bisutils.param.node.RapNamedElement
import com.flipperplz.bisutils.param.node.RapStatement
import com.flipperplz.bisutils.param.utils.ParamElementTypes

interface RapDeleteStatement : RapStatement, RapNamedElement {
    companion object;
    override val slimName: String?

    override fun getRapElementType(): ParamElementTypes = ParamElementTypes.C_DELETE

    fun shouldValidateTarget(): Boolean

    fun locateTargetClass(): RapExternalClass?

    override fun isCurrentlyValid(): Boolean =
        !slimName.isNullOrBlank() && (shouldValidateTarget() && locateTargetClass() != null)

    override fun isBinarizable(): Boolean = true

    override fun toParam(): String = "delete $slimName;"
}