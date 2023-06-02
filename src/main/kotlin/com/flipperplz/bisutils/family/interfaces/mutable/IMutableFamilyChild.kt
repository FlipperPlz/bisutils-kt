package com.flipperplz.bisutils.family.interfaces.mutable

import com.flipperplz.bisutils.family.interfaces.IFamilyChild
import com.flipperplz.bisutils.family.interfaces.IFamilyNode

interface IMutableFamilyChild : IMutableFamilyMember, IFamilyChild {
    override var parent: IFamilyNode?
    override var node: IFamilyNode?
}