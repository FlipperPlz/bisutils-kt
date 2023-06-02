package com.flipperplz.bisutils.bank.ast.entry.mutable

import com.flipperplz.bisutils.bank.ast.entry.IPboVersionEntry
import com.flipperplz.bisutils.bank.ast.misc.mutable.IMutablePboProperty
import com.flipperplz.bisutils.family.interfaces.IFamilyMember
import com.flipperplz.bisutils.family.interfaces.mutable.IMutableFamilyParent

interface IMutablePboVersionEntry : IPboVersionEntry, IMutableFamilyParent {
    override var properties: MutableList<IMutablePboProperty>
    override var children: List<IFamilyMember>?
        get() = properties
        set(value) {
            properties.clear()
            children?.let {
                properties = it.filterIsInstance<IMutablePboProperty>().toMutableList()
            }
        }

}