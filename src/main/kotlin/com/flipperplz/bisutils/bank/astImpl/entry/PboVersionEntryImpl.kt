package com.flipperplz.bisutils.bank.astImpl.entry

import com.flipperplz.bisutils.bank.astImpl.PboEntryImpl
import com.flipperplz.bisutils.bank.ast.PboFile
import com.flipperplz.bisutils.bank.ast.entry.PboVersionEntry
import com.flipperplz.bisutils.bank.ast.misc.PboProperty
import com.flipperplz.bisutils.bank.utils.EntryMimeType
import com.flipperplz.bisutils.family.interfaces.FamilyNode
import java.nio.ByteBuffer
import java.nio.charset.Charset

class PboVersionEntryImpl(
    override val node: FamilyNode?,
    override val parent: FamilyNode?,
    entryName: String, entryMime: EntryMimeType,
    entryDecompressedSize: Long,
    entryOffset: Long,
    entryTimestamp: Long,
    entrySize: Long,
    override val properties: List<PboProperty>
) : PboEntryImpl(node, parent, entryName, entryMime, entryDecompressedSize, entryOffset, entryTimestamp, entrySize), PboVersionEntry {
    override fun read(buffer: ByteBuffer, charset: Charset) {
        TODO("Not yet implemented")
    }
}
