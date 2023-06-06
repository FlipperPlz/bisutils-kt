package com.flipperplz.bisutils.family.mutable

import com.flipperplz.bisutils.family.IFamilyChild

interface IMutableFamilyChild : IMutableFamilyMember, IFamilyChild {
    override var parent: IMutableFamilyParent?
    override var node: IMutableFamilyNode?
}