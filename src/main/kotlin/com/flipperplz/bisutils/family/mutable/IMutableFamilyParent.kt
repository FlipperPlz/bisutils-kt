package com.flipperplz.bisutils.family.mutable

import com.flipperplz.bisutils.family.IFamilyChild
import com.flipperplz.bisutils.family.IFamilyMember
import com.flipperplz.bisutils.family.IFamilyNode
import com.flipperplz.bisutils.family.IFamilyParent

interface IMutableFamilyParent : IMutableFamilyChild, IFamilyParent {
    override var children: MutableList<IMutableFamilyMember>?
}