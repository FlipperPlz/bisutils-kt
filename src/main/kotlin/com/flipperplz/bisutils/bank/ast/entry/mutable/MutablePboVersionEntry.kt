package com.flipperplz.bisutils.bank.ast.entry.mutable

import com.flipperplz.bisutils.bank.ast.entry.PboVersionEntry
import com.flipperplz.bisutils.bank.ast.misc.mutable.MutablePboProperty
import com.flipperplz.bisutils.family.interfaces.FamilyMember
import com.flipperplz.bisutils.family.interfaces.mutable.MutableFamilyParent

interface MutablePboVersionEntry : PboVersionEntry, MutableFamilyParent {
    override var properties: MutableList<MutablePboProperty>
    override var children: List<FamilyMember>?
        get() = properties
        set(value) {
            properties.clear()
            children?.let {
                properties = it.filterIsInstance<MutablePboProperty>().toMutableList()
            }
        }

}