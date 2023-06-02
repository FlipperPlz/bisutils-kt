package com.flipperplz.bisutils.bank.astImpl.entry

import com.flipperplz.bisutils.bank.astImpl.PboEntry
import com.flipperplz.bisutils.bank.ast.entry.IPboVersionEntry
import com.flipperplz.bisutils.bank.ast.misc.IPboProperty
import com.flipperplz.bisutils.bank.utils.EntryMimeType
import com.flipperplz.bisutils.family.interfaces.IFamilyNode
import java.nio.ByteBuffer
import java.nio.charset.Charset

class PboVersionEntry(
    override val node: IFamilyNode?,
    override val parent: IFamilyNode?,
    entryName: String, entryMime: EntryMimeType,
    entryDecompressedSize: Long,
    entryOffset: Long,
    entryTimestamp: Long,
    entrySize: Long,
    override val properties: List<IPboProperty>
) : PboEntry(node, parent, entryName, entryMime, entryDecompressedSize, entryOffset, entryTimestamp, entrySize), IPboVersionEntry {
    override fun read(buffer: ByteBuffer, charset: Charset) = throw Exception("Not Supported")
}
