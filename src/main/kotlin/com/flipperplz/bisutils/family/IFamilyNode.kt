package com.flipperplz.bisutils.family

interface IFamilyNode : IFamilyMember {
    val children: List<IFamilyMember>?
}