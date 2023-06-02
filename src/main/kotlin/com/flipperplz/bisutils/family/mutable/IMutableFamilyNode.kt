package com.flipperplz.bisutils.family.mutable

import com.flipperplz.bisutils.family.IFamilyMember
import com.flipperplz.bisutils.family.IFamilyNode

interface IMutableFamilyNode : IMutableFamilyMember, IFamilyNode {
    override var children: List<IFamilyMember>?
}