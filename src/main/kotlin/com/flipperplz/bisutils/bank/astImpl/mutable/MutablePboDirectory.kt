package com.flipperplz.bisutils.bank.astImpl.mutable

import com.flipperplz.bisutils.bank.ast.mutable.IMutablePboDirectory
import com.flipperplz.bisutils.bank.astImpl.PboDirectory
import com.flipperplz.bisutils.family.IFamilyMember
import com.flipperplz.bisutils.family.IFamilyNode

class MutablePboDirectory(
    override var node: IFamilyNode?,
    override var parent: IFamilyNode?,
    override var children: List<IFamilyMember>?) : PboDirectory(node, parent,
    children
), IMutablePboDirectory {
}