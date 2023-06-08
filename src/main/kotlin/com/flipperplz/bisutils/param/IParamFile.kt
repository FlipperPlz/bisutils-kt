package com.flipperplz.bisutils.param

import com.flipperplz.bisutils.family.IFamilyNode
import com.flipperplz.bisutils.family.IFamilyParent
import com.flipperplz.bisutils.param.ast.node.IParamElement
import com.flipperplz.bisutils.param.ast.node.IParamNamedElement
import com.flipperplz.bisutils.param.ast.node.IParamStatementHolder
import com.flipperplz.bisutils.param.utils.ParamElementTypes

interface IParamFile : IFamilyNode, IParamStatementHolder, IParamNamedElement {
    companion object;
    override val parent: IFamilyParent?
        get() = null

    override val node: IParamFile?
        get() = this

    override fun toParam(): String = super.toParam()
}