package com.flipperplz.bisutils.family.mutable

import com.flipperplz.bisutils.family.IFamilyChild
import com.flipperplz.bisutils.family.IFamilyNode

interface IMutableFamilyChild : IMutableFamilyMember, IFamilyChild {
    override var parent: IFamilyNode?
    override var node: IFamilyNode?
}