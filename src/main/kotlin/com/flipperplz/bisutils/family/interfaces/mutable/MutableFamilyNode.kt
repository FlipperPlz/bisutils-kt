package com.flipperplz.bisutils.family.interfaces.mutable

import com.flipperplz.bisutils.family.interfaces.FamilyMember
import com.flipperplz.bisutils.family.interfaces.FamilyNode

interface MutableFamilyNode : MutableFamilyMember, FamilyNode {
    override var children: List<FamilyMember>?
}