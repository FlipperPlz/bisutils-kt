package com.flipperplz.bisutils.family.interfaces

interface FamilyParent : FamilyNode, FamilyChild {
    override val parent: FamilyNode?
    override val children: List<FamilyMember>?
}