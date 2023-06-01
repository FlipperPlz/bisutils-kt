package com.flipperplz.bisutils.bank.ast.mutable

import com.flipperplz.bisutils.bank.ast.PboEntry
import com.flipperplz.bisutils.bank.ast.misc.mutable.MutablePboElement
import com.flipperplz.bisutils.bank.utils.EntryMimeType
import com.flipperplz.bisutils.utils.BisFlushable

interface MutablePboEntry : MutablePboElement, PboEntry, BisFlushable {
    override var entrySize: Long
    override var entryName: String
    override var entryOffset: Long
    override var entryDecompressedSize: Long
    override var entryMime: EntryMimeType
    override var entryTimestamp: Long
    override val children: MutableList<Any>?
        get() = mutableListOf(entrySize, entryName, entryOffset, entryDecompressedSize, entryMime, entryTimestamp)

    override fun flush() {
        entryMime = EntryMimeType.DUMMY
        entrySize = 0
        entryName = ""
        entryOffset = 0
        entryDecompressedSize = 0
        entryTimestamp = 0
    }
}