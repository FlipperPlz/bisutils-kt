package com.flipperplz.bisutils.bank.astImpl

import com.flipperplz.bisutils.bank.ast.IPboDirectory
import com.flipperplz.bisutils.family.IFamilyMember
import com.flipperplz.bisutils.family.IFamilyNode

open class PboDirectory(
    override val node: IFamilyNode?,
    override val parent: IFamilyNode?,
    override val children: List<IFamilyMember>?
) : IPboDirectory