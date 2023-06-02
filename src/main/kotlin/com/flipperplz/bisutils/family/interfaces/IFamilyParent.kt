package com.flipperplz.bisutils.family.interfaces

interface IFamilyParent : IFamilyNode, IFamilyChild {
    override val parent: IFamilyNode?
    override val children: List<IFamilyMember>?
}