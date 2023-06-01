package com.flipperplz.bisutils.bank.ast.mutable

import com.flipperplz.bisutils.bank.ast.PboEntry
import com.flipperplz.bisutils.bank.ast.PboFile
import com.flipperplz.bisutils.bank.ast.misc.PboProperty
import com.flipperplz.bisutils.collections.BisMutableUpcastedList
import com.flipperplz.bisutils.family.interfaces.FamilyChild
import com.flipperplz.bisutils.family.interfaces.FamilyMember
import com.flipperplz.bisutils.family.interfaces.FamilyNode
import com.flipperplz.bisutils.family.interfaces.mutable.MutableFamilyParent
import com.flipperplz.bisutils.utils.BisFlushable

interface MutablePboFile : MutableFamilyParent, PboFile, BisFlushable {
    override var entries: MutableList<PboEntry>
    override var signature: ByteArray
    override var children: List<FamilyMember>?
        get() = entries
        set(value) {
            entries.clear()
            children?.let {
                entries = it.filterIsInstance<PboEntry>().toMutableList()
            }
        }

    override fun flush() {
        entries.filterIsInstance<BisFlushable>().forEach { it.flush() }
        signature = byteArrayOf()
    }
}