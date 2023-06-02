package com.flipperplz.bisutils.family.interfaces

interface IFamilyNode : IFamilyMember {
    val children: List<IFamilyMember>?
}