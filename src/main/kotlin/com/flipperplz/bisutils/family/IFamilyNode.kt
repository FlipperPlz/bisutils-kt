package com.flipperplz.bisutils.family

interface IFamilyNode : IFamilyParent {
    override val parent: IFamilyParent?
        get() = null
    override val node: IFamilyNode?
        get() = null
    override val children: List<IFamilyMember>?

}