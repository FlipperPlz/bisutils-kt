package com.flipperplz.bisutils.family

interface IFamilyParent : IFamilyChild {
    override val parent: IFamilyParent?
    val children: List<IFamilyMember>?
}