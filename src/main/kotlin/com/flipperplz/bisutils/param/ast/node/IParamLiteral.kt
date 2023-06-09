package com.flipperplz.bisutils.param.ast.node

import com.flipperplz.bisutils.family.IFamilyChild
import com.flipperplz.bisutils.options.IOptions

interface IParamLiteral : IParamElement, IFamilyChild {
    override val parent: IParamLiteralParent?
    val paramValue: Any?

    override fun isValid(options: IOptions?): Boolean = paramValue != null
}