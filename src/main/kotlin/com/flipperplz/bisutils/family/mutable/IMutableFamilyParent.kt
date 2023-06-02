package com.flipperplz.bisutils.family.mutable

import com.flipperplz.bisutils.family.IFamilyMember
import com.flipperplz.bisutils.family.IFamilyNode
import com.flipperplz.bisutils.family.IFamilyParent

interface IMutableFamilyParent : IMutableFamilyNode, IMutableFamilyChild, IFamilyParent {
    override var children: List<IFamilyMember>?
    override var node: IFamilyNode?
    override var parent: IFamilyNode?
}