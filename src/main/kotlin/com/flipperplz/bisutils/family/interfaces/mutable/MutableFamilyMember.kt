package com.flipperplz.bisutils.family.interfaces.mutable

import com.flipperplz.bisutils.family.interfaces.FamilyMember
import com.flipperplz.bisutils.family.interfaces.FamilyNode

interface MutableFamilyMember : FamilyMember {
    override var node: FamilyNode?
}