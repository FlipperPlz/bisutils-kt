package com.flipperplz.bisutils.bank.astImpl

import com.flipperplz.bisutils.bank.ast.IPboEntry
import com.flipperplz.bisutils.bank.utils.EntryMimeType
import com.flipperplz.bisutils.family.interfaces.IFamilyNode
import com.flipperplz.bisutils.binarization.BisStrictDebinarizable
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
) : BisStrictDebinarizable(), IPboEntry {
    override fun read(buffer: ByteBuffer, charset: Charset): Boolean = throw Exception("Not supported")
}