package com.flipperplz.bisutils.family.mutable

import com.flipperplz.bisutils.family.IFamilyMember
import com.flipperplz.bisutils.family.IFamilyNode

interface IMutableFamilyMember : IFamilyMember {
    override var node: IFamilyNode?
}