package com.flipperplz.bisutils.param.ast.node

import com.flipperplz.bisutils.family.IFamilyParent

interface IParamLiteralParent : IParamElement, IFamilyParent {
    override val familyChildren: List<IParamLiteral>?
}