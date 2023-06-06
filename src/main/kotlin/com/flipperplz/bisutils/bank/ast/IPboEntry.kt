package com.flipperplz.bisutils.bank.ast

import com.flipperplz.bisutils.bank.options.PboEntryBinarizationOptions
import com.flipperplz.bisutils.bank.options.PboEntryDebinarizationOptions
import com.flipperplz.bisutils.bank.utils.EntryMimeType
import com.flipperplz.bisutils.binarization.options.DEFAULT_BIS_CHARSET
import com.flipperplz.bisutils.binarization.options.DEFAULT_BIS_ENDIANNESS
import com.flipperplz.bisutils.io.putAsciiZ
import com.flipperplz.bisutils.io.putLong
import com.flipperplz.bisutils.options.BisOptions
import java.nio.ByteBuffer

interface IPboEntry : IPboVFSEntry {
    override val parent: IPboDirectory?
    override val node: IPboFile?
    override val entryName: String
    override val absolutePath: String
    override val path: String
    val entryMime: EntryMimeType
    val entryTimestamp: Long
    val entryOffset: Long
    val entryDecompressedSize: Long
    val entrySize: Long

    override fun writeValidated(buffer: ByteBuffer, options: PboEntryBinarizationOptions?): Boolean {
        buffer.putAsciiZ(options?.entryName ?: entryName, options?.charset ?: DEFAULT_BIS_CHARSET)
        if(!(options?.entryMime ?: entryMime).write(buffer, options)) return false
        buffer.putLong(options?.entryOriginalSize ?: entryDecompressedSize, options?.endianness ?: DEFAULT_BIS_ENDIANNESS)
        buffer.putLong(options?.entryOffset ?: entryOffset, options?.endianness ?: DEFAULT_BIS_ENDIANNESS)
        buffer.putLong(options?.entryTimestamp ?: entryTimestamp, options?.endianness ?: DEFAULT_BIS_ENDIANNESS)
        buffer.putLong(options?.entrySize ?: entrySize, options?.endianness ?: DEFAULT_BIS_ENDIANNESS)
        return true
    }

    override fun isValid(options: BisOptions?): Boolean =
        entryTimestamp >= 0 && entryOffset >= 0 && entryDecompressedSize >= 0 && entrySize >= 0

    override fun read(buffer: ByteBuffer, options: PboEntryDebinarizationOptions): Boolean =
        throw Exception("Not Supported!")

    override fun calculateBinaryLength(options: PboEntryBinarizationOptions?): Long = (options?.charset ?: DEFAULT_BIS_CHARSET).newEncoder().averageBytesPerChar().let {
        (entryName.length * it).toLong() + 21
    }
}
