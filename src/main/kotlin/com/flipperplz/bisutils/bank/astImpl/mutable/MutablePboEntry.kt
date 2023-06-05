package com.flipperplz.bisutils.bank.astImpl.mutable

import com.flipperplz.bisutils.bank.astImpl.PboEntry
import com.flipperplz.bisutils.bank.ast.mutable.IMutablePboEntry
import com.flipperplz.bisutils.bank.options.PboEntryDebinOptions
import com.flipperplz.bisutils.bank.utils.EntryMimeType
import com.flipperplz.bisutils.family.IFamilyNode
import com.flipperplz.bisutils.io.getAsciiZ
import com.flipperplz.bisutils.io.getInt
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

    constructor(node: IFamilyNode?, parent: IFamilyNode?, buffer: ByteBuffer, options: PboEntryDebinOptions) : this(
        node,
        parent,
        options.entryName ?: buffer.getAsciiZ(options.charset),
        options.entryMime ?: EntryMimeType.fromMime(buffer.getInt(options.endianness)) ?: throw Exception(),
        options.entryOriginalSize ?: buffer.getLong(), //TODO endianness
        options.entryOffset ?: buffer.getLong(),
        options.entryTimestamp ?: buffer.getLong(),
        options.entrySize ?: buffer.getLong()
    )

    override fun read(buffer: ByteBuffer, charset: Charset, options: PboEntryDebinOptions): Boolean {
        entryName = options.entryName ?: buffer.getAsciiZ(options.charset)
        entryMime = options.entryMime ?: EntryMimeType.fromMime(buffer.getInt(options.endianness)) ?: return false
        entryDecompressedSize = options.entryOriginalSize ?: buffer.getLong()
        entryOffset = options.entryOffset ?: buffer.getLong()
        entryTimestamp = options.entryTimestamp ?: buffer.getLong()
        entrySize = options.entrySize ?: buffer.getLong()
        return true
    }
}