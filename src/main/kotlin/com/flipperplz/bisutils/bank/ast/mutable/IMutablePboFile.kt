package com.flipperplz.bisutils.bank.ast.mutable

import com.flipperplz.bisutils.bank.ast.IPboEntry
import com.flipperplz.bisutils.bank.ast.IPboFile
import com.flipperplz.bisutils.family.IFamilyMember
import com.flipperplz.bisutils.family.mutable.IMutableFamilyParent
import com.flipperplz.bisutils.utils.IFlushable

interface IMutablePboFile : IMutableFamilyParent, IPboFile, IFlushable {
    override var entries: MutableList<IPboEntry>
    override var signature: ByteArray
    override var children: List<IFamilyMember>?
        get() = entries
        set(value) {
            entries.clear()
            children?.let {
                entries = it.filterIsInstance<IPboEntry>().toMutableList()
            }
        }

    override fun flush() {
        entries.filterIsInstance<IFlushable>().forEach { it.flush() }
        signature = byteArrayOf()
    }
}