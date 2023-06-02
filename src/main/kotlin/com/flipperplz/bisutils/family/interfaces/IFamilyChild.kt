package com.flipperplz.bisutils.family.interfaces

interface IFamilyChild : IFamilyMember {
    val parent: IFamilyNode?
    override val node: IFamilyNode?
}