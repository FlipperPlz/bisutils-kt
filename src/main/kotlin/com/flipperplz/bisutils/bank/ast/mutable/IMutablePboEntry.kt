package com.flipperplz.bisutils.bank.ast.mutable

import com.flipperplz.bisutils.bank.ast.IPboEntry
import com.flipperplz.bisutils.bank.ast.entry.mutable.IMutablePboDataEntry
import com.flipperplz.bisutils.bank.options.PboEntryDebinarizationOptions
import com.flipperplz.bisutils.bank.utils.EntryMimeType
import com.flipperplz.bisutils.binarization.options.DEFAULT_BIS_CHARSET
import com.flipperplz.bisutils.binarization.options.DEFAULT_BIS_ENDIANNESS
import com.flipperplz.bisutils.io.putAsciiZ
import com.flipperplz.bisutils.io.putLong
import com.flipperplz.bisutils.utils.IFlushable
import java.nio.ByteBuffer

interface IMutablePboEntry : IPboEntry, IMutablePboVFSEntry, IFlushable, Cloneable {
    override var parent: IMutablePboDirectory?
    override var node: IMutablePboFile?
    override val absolutePath: String
    override val path: String
    override var entryName: String
    override var entryMime: EntryMimeType
    override var entryDecompressedSize: Long
    override var entryTimestamp: Long
    override var entryOffset: Long
    override var entrySize: Long

    override fun read(buffer: ByteBuffer, options: PboEntryDebinarizationOptions): Boolean {
        buffer.putAsciiZ(options.entryName ?: entryName, options.charset ?: DEFAULT_BIS_CHARSET)
        buffer.putLong(options.entryMime?.mime ?: entryMime.mime, options.endianness ?: DEFAULT_BIS_ENDIANNESS)
        buffer.putLong(options.entryOriginalSize ?: entryDecompressedSize, options.endianness ?: DEFAULT_BIS_ENDIANNESS)
        buffer.putLong(options.entryTimestamp ?: entryTimestamp, options.endianness ?: DEFAULT_BIS_ENDIANNESS)
        buffer.putLong(options.entryOffset ?: entryOffset, options.endianness ?: DEFAULT_BIS_ENDIANNESS)
        buffer.putLong(options.entrySize ?: entrySize, options.endianness ?: DEFAULT_BIS_ENDIANNESS)
        return true
    }

    override fun flush() {
        entryOffset = 0; entryMime = EntryMimeType.DUMMY; entryName = ""
        entryDecompressedSize = 0; entrySize = 0; entryTimestamp = 0
    }

    override fun clone(): IMutablePboEntry = super<Cloneable>.clone() as IMutablePboEntry

}