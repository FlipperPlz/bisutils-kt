package com.flipperplz.bisutils.family

interface IFamilyChild : IFamilyMember {
    val parent: IFamilyNode?
    override val node: IFamilyNode?
}