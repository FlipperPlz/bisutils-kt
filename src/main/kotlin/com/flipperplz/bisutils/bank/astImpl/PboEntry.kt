package com.flipperplz.bisutils.bank.astImpl

import com.flipperplz.bisutils.bank.ast.IPboEntry
import com.flipperplz.bisutils.bank.options.PboDebinarizationOptions
import com.flipperplz.bisutils.bank.utils.EntryMimeType
import com.flipperplz.bisutils.bank.utils.IPboBinaryObject
import com.flipperplz.bisutils.family.IFamilyNode
import java.nio.ByteBuffer
import java.nio.charset.Charset

abstract class PboEntry(
    override val node: IFamilyNode?,
    override val parent: IFamilyNode?,
    override val entryName: String,
    override val entryMime: EntryMimeType,
    override val entryDecompressedSize: Long,
    override val entryOffset: Long,
    override val entryTimestamp: Long,
    override val entrySize: Long
) : IPboBinaryObject, IPboEntry {
    override fun read(buffer: ByteBuffer, charset: Charset, options: PboDebinarizationOptions): Boolean = throw Exception("Not supported")
}