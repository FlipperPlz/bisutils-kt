package com.flipperplz.bisutils.family.interfaces.mutable

import com.flipperplz.bisutils.family.interfaces.IFamilyMember
import com.flipperplz.bisutils.family.interfaces.IFamilyNode

interface IMutableFamilyNode : IMutableFamilyMember, IFamilyNode {
    override var children: List<IFamilyMember>?
}