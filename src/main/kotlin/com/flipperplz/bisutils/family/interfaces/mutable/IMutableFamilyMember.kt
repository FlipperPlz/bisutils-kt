package com.flipperplz.bisutils.family.interfaces.mutable

import com.flipperplz.bisutils.family.interfaces.IFamilyMember
import com.flipperplz.bisutils.family.interfaces.IFamilyNode

interface IMutableFamilyMember : IFamilyMember {
    override var node: IFamilyNode?
}