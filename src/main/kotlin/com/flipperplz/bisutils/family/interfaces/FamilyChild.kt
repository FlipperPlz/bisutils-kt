package com.flipperplz.bisutils.family.interfaces

interface FamilyChild : FamilyMember {
    val parent: FamilyNode?
    override val node: FamilyNode?
}