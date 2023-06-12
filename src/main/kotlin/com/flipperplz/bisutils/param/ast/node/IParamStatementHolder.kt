package com.flipperplz.bisutils.param.ast.node

import com.flipperplz.bisutils.family.IFamilyParent
import com.flipperplz.bisutils.options.IOptions

interface IParamStatementHolder : IParamElement, IFamilyParent {
    override val familyChildren: List<IParamStatement>
    override fun isValid(options: IOptions?): Boolean = familyChildren.all { it.isValid(options) }
    override fun toParam(): String = familyChildren.joinToString(separator = "\n", postfix = "\n") { it.toParam() }
}