package com.flipperplz.bisutils.bank.ast

import com.flipperplz.bisutils.bank.options.PboBinarizationOptions
import com.flipperplz.bisutils.bank.utils.EntryMimeType
import com.flipperplz.bisutils.bank.utils.IPboBinaryObject
import com.flipperplz.bisutils.family.IFamilyChild
import com.flipperplz.bisutils.io.putAsciiZ
import java.nio.ByteBuffer
import java.nio.charset.Charset

interface IPboEntry : IPboBinaryObject, IFamilyChild {
    val entryName: String
    val entryMime: EntryMimeType
    val entryTimestamp: Long
    val entryOffset: Long
    val entryDecompressedSize: Long
    val entrySize: Long

    override fun writeValidated(buffer: ByteBuffer, charset: Charset, options: PboBinarizationOptions?): Boolean {
        buffer.putAsciiZ(entryName, charset)
        if(!entryMime.write(buffer, charset, options)) return false
        buffer.putLong(entryDecompressedSize)
        buffer.putLong(entryOffset)
        buffer.putLong(entryTimestamp)
        buffer.putLong(entrySize)
        return true
    }

    override fun calculateBinaryLength(charset: Charset, options: PboBinarizationOptions?): Long =
        21L + entryName.length

}