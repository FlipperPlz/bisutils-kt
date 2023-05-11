package com.flipperplz.bisutils.param.statement

import com.flipperplz.bisutils.param.node.RapNamedElement
import com.flipperplz.bisutils.param.node.RapStatement
import com.flipperplz.bisutils.param.utils.ParamElementTypes

interface RapExternalClass : RapStatement, RapNamedElement {

    override fun getRapElementType(): ParamElementTypes = ParamElementTypes.C_CLASS_EXTERNAL

    override fun isCurrentlyValid(): Boolean = slimName != null

    override fun toParam(): String = "class $slimName;"
}