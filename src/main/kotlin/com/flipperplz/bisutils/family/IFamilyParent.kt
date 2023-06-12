package com.flipperplz.bisutils.family

interface IFamilyParent : IFamilyChild {
    override val familyParent: IFamilyParent?
    val familyChildren: List<IFamilyMember>?
}