package com.flipperplz.bisutils.bank.astImpl.mutable

import com.flipperplz.bisutils.bank.astImpl.PboEntryImpl
import com.flipperplz.bisutils.bank.ast.mutable.MutablePboEntry
import com.flipperplz.bisutils.bank.utils.EntryMimeType
import com.flipperplz.bisutils.family.interfaces.FamilyNode
import java.nio.ByteBuffer
import java.nio.charset.Charset

abstract class MutablePboEntryImpl(
    override var node: FamilyNode?,
    override var parent: FamilyNode?,
    override var entryName: String,
    override var entryMime: EntryMimeType,
    override var entryDecompressedSize: Long,
    override var entryOffset: Long,
    override var entryTimestamp: Long,
    override var entrySize: Long
) : PboEntryImpl(
    node, parent, entryName, entryMime, entryDecompressedSize, entryOffset, entryTimestamp, entrySize
), MutablePboEntry {
    override fun read(buffer: ByteBuffer, charset: Charset) {
        TODO()
    }
}