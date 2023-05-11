package com.flipperplz.bisutils.param.directive

import com.flipperplz.bisutils.param.literal.RapArray
import com.flipperplz.bisutils.param.node.RapDirective
import com.flipperplz.bisutils.param.node.RapElement
import com.flipperplz.bisutils.param.node.RapLiteral
import com.flipperplz.bisutils.param.node.RapNamedElement
import com.flipperplz.bisutils.param.statement.RapVariableStatement
import com.flipperplz.bisutils.param.utils.ParamElementTypes

interface RapInclude : RapDirective, RapLiteral<List<RapElement>>, RapNamedElement {
    override fun getRapElementType(): ParamElementTypes =
        if(isLiteralValue())
            ParamElementTypes.L_INCLUDE else
            ParamElementTypes.C_INCLUDE

    fun isLiteralValue(): Boolean =
        slimParent is RapArray || slimParent is RapVariableStatement

    fun shouldValidateInclude() : Boolean

    override fun isCurrentlyValid(): Boolean =
        super.isCurrentlyValid() &&
        !slimName.isNullOrBlank() && (
            !shouldValidateInclude() ||
            slimValue != null
        )

    override fun toParam(): String =
        "#include <$slimValue>${if(!isLiteralValue()) "\n" else ""}"
}