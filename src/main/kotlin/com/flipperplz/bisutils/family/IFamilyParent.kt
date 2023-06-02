package com.flipperplz.bisutils.family

interface IFamilyParent : IFamilyNode, IFamilyChild {
    override val parent: IFamilyNode?
    override val children: List<IFamilyMember>?
}