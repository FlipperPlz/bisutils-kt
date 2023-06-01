package com.flipperplz.bisutils.family.interfaces.mutable

import com.flipperplz.bisutils.family.interfaces.FamilyChild
import com.flipperplz.bisutils.family.interfaces.FamilyNode

interface MutableFamilyChild : MutableFamilyMember, FamilyChild {
    override var parent: FamilyNode?
    override var node: FamilyNode?
}