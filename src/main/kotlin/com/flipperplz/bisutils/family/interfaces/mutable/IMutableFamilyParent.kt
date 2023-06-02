package com.flipperplz.bisutils.family.interfaces.mutable

import com.flipperplz.bisutils.family.interfaces.IFamilyMember
import com.flipperplz.bisutils.family.interfaces.IFamilyNode
import com.flipperplz.bisutils.family.interfaces.IFamilyParent

interface IMutableFamilyParent : IMutableFamilyNode, IMutableFamilyChild, IFamilyParent {
    override var children: List<IFamilyMember>?
    override var node: IFamilyNode?
    override var parent: IFamilyNode?
}