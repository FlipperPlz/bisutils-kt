package com.flipperplz.bisutils.param.ast.node

import com.flipperplz.bisutils.family.IFamilyParent
import com.flipperplz.bisutils.options.IOptions

interface IParamStatementHolder : IParamElement, IFamilyParent {
    override val children: List<IParamStatement>
    override fun isValid(options: IOptions?): Boolean = children.all { it.isValid(options) }
    override fun isBinarizable(): Boolean = children.all { it.isBinarizable() }

    override fun toParam(): String = children.joinToString(separator = "\n", postfix = "\n") { it.toParam() }
}