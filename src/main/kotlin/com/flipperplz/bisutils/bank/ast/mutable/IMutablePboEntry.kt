package com.flipperplz.bisutils.bank.ast.mutable

import com.flipperplz.bisutils.bank.ast.IPboEntry
import com.flipperplz.bisutils.bank.utils.EntryMimeType
import com.flipperplz.bisutils.family.interfaces.mutable.IMutableFamilyChild
import com.flipperplz.bisutils.utils.IFlushable

interface IMutablePboEntry : IMutableFamilyChild, IPboEntry, IFlushable {
    override var entrySize: Long
    override var entryName: String
    override var entryOffset: Long
    override var entryDecompressedSize: Long
    override var entryMime: EntryMimeType
    override var entryTimestamp: Long

    override fun flush() {
        entryMime = EntryMimeType.DUMMY
        entrySize = 0
        entryName = ""
        entryOffset = 0
        entryDecompressedSize = 0
        entryTimestamp = 0
    }

    override val binaryLength: Long
        get() = 21L + entryName.length
}