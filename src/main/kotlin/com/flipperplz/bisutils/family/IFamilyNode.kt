package com.flipperplz.bisutils.family

interface IFamilyNode : IFamilyParent {
    override val familyParent: IFamilyParent?
        get() = null
    override val familyNode: IFamilyNode?
        get() = null
    override val familyChildren: List<IFamilyMember>?

}