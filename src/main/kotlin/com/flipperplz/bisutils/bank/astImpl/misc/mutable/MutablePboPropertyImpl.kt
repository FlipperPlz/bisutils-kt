package com.flipperplz.bisutils.bank.astImpl.misc.mutable

import com.flipperplz.bisutils.bank.ast.misc.mutable.MutablePboProperty
import com.flipperplz.bisutils.bank.astImpl.misc.PboPropertyImpl
import com.flipperplz.bisutils.family.interfaces.FamilyNode

class MutablePboPropertyImpl(
    override var parent: FamilyNode?,
    override var node: FamilyNode?,
    override var name: String,
    override var value: String
) : PboPropertyImpl(parent, node, name, value), MutablePboProperty