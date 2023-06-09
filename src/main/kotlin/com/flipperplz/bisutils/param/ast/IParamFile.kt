package com.flipperplz.bisutils.param.ast

import com.flipperplz.bisutils.family.IFamilyNode
import com.flipperplz.bisutils.family.IFamilyParent
import com.flipperplz.bisutils.param.ast.node.IParamStatementHolder

interface IParamFile : IFamilyNode, IParamStatementHolder {
    companion object;
    val paramName: String?
    override val parent: IFamilyParent? get() = null
    override val node: IParamFile? get() = this

    override fun toParam(): String = super.toParam()
}