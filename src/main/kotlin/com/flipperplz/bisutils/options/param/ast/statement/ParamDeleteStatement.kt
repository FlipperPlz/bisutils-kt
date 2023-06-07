package com.flipperplz.bisutils.options.param.ast.statement

import com.flipperplz.bisutils.options.param.ast.node.ParamNamedElement
import com.flipperplz.bisutils.options.param.ast.node.ParamStatement
import com.flipperplz.bisutils.options.param.utils.ParamElementTypes

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