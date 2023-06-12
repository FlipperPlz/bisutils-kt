package com.flipperplz.bisutils.param.ast.node

import com.flipperplz.bisutils.family.IFamilyChild

interface IParamStatement : IParamElement, IFamilyChild {
    override val familyParent: IParamStatementHolder?
    companion object
}