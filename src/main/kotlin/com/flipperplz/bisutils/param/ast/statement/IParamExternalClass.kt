package com.flipperplz.bisutils.param.ast.statement

import com.flipperplz.bisutils.options.IOptions
import com.flipperplz.bisutils.param.ast.node.IParamNamedElement
import com.flipperplz.bisutils.param.ast.node.IParamStatement
import com.flipperplz.bisutils.param.utils.ParamElementTypes

interface IParamExternalClass : IParamStatement, IParamNamedElement {
    companion object;
    override fun isValid(options: IOptions?): Boolean = slimName != null
    override fun isBinarizable(): Boolean  = true
    override fun toParam(): String = "class $slimName;"
}