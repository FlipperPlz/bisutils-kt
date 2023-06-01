package com.flipperplz.bisutils.family.interfaces

interface FamilyNode : FamilyMember {
    val children: List<FamilyMember>?
}