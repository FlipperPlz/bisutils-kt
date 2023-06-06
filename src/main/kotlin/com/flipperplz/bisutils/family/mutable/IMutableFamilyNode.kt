package com.flipperplz.bisutils.family.mutable

import com.flipperplz.bisutils.family.IFamilyNode

interface IMutableFamilyNode : IMutableFamilyParent, IFamilyNode {
    override var parent: IMutableFamilyParent?
    override var children: MutableList<IMutableFamilyMember>?
    override var node: IMutableFamilyNode?
}