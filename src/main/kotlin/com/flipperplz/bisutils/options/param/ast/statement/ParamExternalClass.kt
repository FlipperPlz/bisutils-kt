package com.flipperplz.bisutils.options.param.ast.statement

import com.flipperplz.bisutils.options.param.ast.node.ParamNamedElement
import com.flipperplz.bisutils.options.param.ast.node.ParamStatement
import com.flipperplz.bisutils.options.param.utils.ParamElementTypes

//TODO: Validate name exists in master cfg
interface ParamExternalClass : ParamStatement, ParamNamedElement {
    companion object;

    override fun getParamElementType(): ParamElementTypes = ParamElementTypes.C_CLASS_EXTERNAL

    override fun isCurrentlyValid(): Boolean = slimName != null

    override fun isBinarizable(): Boolean  = true

    override fun toParam(): String = "class $slimName;"
}