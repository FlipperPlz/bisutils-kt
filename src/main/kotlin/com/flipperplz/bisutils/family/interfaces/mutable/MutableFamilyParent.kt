package com.flipperplz.bisutils.family.interfaces.mutable

import com.flipperplz.bisutils.family.interfaces.FamilyMember
import com.flipperplz.bisutils.family.interfaces.FamilyNode
import com.flipperplz.bisutils.family.interfaces.FamilyParent

interface MutableFamilyParent : MutableFamilyNode, MutableFamilyChild, FamilyParent {
    override var children: List<FamilyMember>?
    override var node: FamilyNode?
    override var parent: FamilyNode?
}