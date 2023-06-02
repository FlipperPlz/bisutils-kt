package com.flipperplz.bisutils.bank.astImpl.mutable

import com.flipperplz.bisutils.bank.astImpl.PboEntry
import com.flipperplz.bisutils.bank.ast.mutable.IMutablePboEntry
import com.flipperplz.bisutils.bank.utils.EntryMimeType
import com.flipperplz.bisutils.family.interfaces.IFamilyNode
import com.flipperplz.bisutils.io.getAsciiZ
import com.flipperplz.bisutils.io.peek
import java.nio.ByteBuffer
import java.nio.charset.Charset

abstract class MutablePboEntry(
    override var node: IFamilyNode?,
    override var parent: IFamilyNode?,
    override var entryName: String,
    override var entryMime: EntryMimeType,
    override var entryDecompressedSize: Long,
    override var entryOffset: Long,
    override var entryTimestamp: Long,
    override var entrySize: Long
) : PboEntry(node, parent, entryName, entryMime, entryDecompressedSize, entryOffset, entryTimestamp, entrySize), IMutablePboEntry {
    override fun read(buffer: ByteBuffer, charset: Charset): Boolean { //todo: abstract factory read
        entryName = buffer.getAsciiZ(charset)
        entryMime = EntryMimeType.fromMime(buffer.getInt()) ?: return false
        entryDecompressedSize = buffer.getLong()
        entryOffset = buffer.getLong()
        entryTimestamp = buffer.getLong()
        entrySize = buffer.getLong()
        return true
    }
}