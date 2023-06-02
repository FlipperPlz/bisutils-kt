package com.flipperplz.bisutils.bank.astImpl.entry.mutable

import com.flipperplz.bisutils.bank.ast.entry.mutable.IMutablePboDataEntry
import com.flipperplz.bisutils.bank.astImpl.mutable.MutablePboEntry
import com.flipperplz.bisutils.bank.options.PboDebinarizationOptions
import com.flipperplz.bisutils.bank.utils.EntryMimeType
import com.flipperplz.bisutils.family.IFamilyNode
import com.flipperplz.bisutils.io.getBytes
import java.nio.ByteBuffer
import java.nio.charset.Charset

class MutablePboDataEntry(
    node: IFamilyNode?,
    parent: IFamilyNode?,
    override var entryData: ByteBuffer,
    entryName: String,
    entryMime: EntryMimeType,
    entryDecompressedSize: Long,
    entryOffset: Long,
    entryTimestamp: Long,
    entrySize: Long,
) : MutablePboEntry(node, parent, entryName, entryMime, entryDecompressedSize, entryOffset, entryTimestamp, entrySize), IMutablePboDataEntry {
    override fun read(buffer: ByteBuffer, charset: Charset, options: PboDebinarizationOptions): Boolean {
        if(buffer.remaining() <= entryDecompressedSize || entryDecompressedSize >= Int.MAX_VALUE) return false
        entryData = ByteBuffer.wrap(buffer.getBytes(entryDecompressedSize.toInt()))
        return true
    }

    private fun readData(buffer: ByteBuffer, charset: Charset): Boolean {
        entryData = ByteBuffer.wrap(buffer.getBytes(entryDecompressedSize.toInt())) //TODO: DECOMPRESS
        return true
    }
}