package com.flipperplz.bisutils.bank.astImpl.entry.mutable

import com.flipperplz.bisutils.bank.ast.entry.mutable.IMutablePboDataEntry
import com.flipperplz.bisutils.bank.astImpl.mutable.MutablePboEntry
import com.flipperplz.bisutils.bank.utils.EntryMimeType
import com.flipperplz.bisutils.family.interfaces.IFamilyNode
import com.flipperplz.bisutils.io.getBytes
import com.flipperplz.bisutils.param.lexer.ParamLexer
import java.nio.ByteBuffer
import java.nio.charset.Charset

class MutablePboDataEntry(
    node: IFamilyNode?,
    parent: IFamilyNode?,
    entryName: String,
    entryMime: EntryMimeType,
    entryDecompressedSize: Long,
    entryOffset: Long,
    entryTimestamp: Long,
    entrySize: Long,
) : MutablePboEntry(node, parent, entryName, entryMime, entryDecompressedSize, entryOffset, entryTimestamp, entrySize), IMutablePboDataEntry {
    override lateinit var entryData: ByteBuffer

    private fun readData(buffer: ByteBuffer, charset: Charset): Boolean {
        entryData = ByteBuffer.wrap(buffer.getBytes(entryDecompressedSize.toInt())) //TODO: DECOMPRESS
        return true
    }
}