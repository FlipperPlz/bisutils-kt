package com.flipperplz.bisutils.bank.ast.mutable

import com.flipperplz.bisutils.bank.ast.IPboDirectory
import com.flipperplz.bisutils.bank.ast.IPboEntry
import com.flipperplz.bisutils.bank.ast.IPboFile
import com.flipperplz.bisutils.bank.ast.entry.mutable.IMutablePboDataEntry
import com.flipperplz.bisutils.bank.options.PboOptions
import com.flipperplz.bisutils.bank.utils.EntryMimeType
import com.flipperplz.bisutils.binarization.options.IBinarizationOptions.Companion.DEFAULT_BIS_CHARSET
import com.flipperplz.bisutils.binarization.options.IBinarizationOptions.Companion.DEFAULT_BIS_ENDIANNESS
import com.flipperplz.bisutils.io.getAsciiZ
import com.flipperplz.bisutils.io.getLong
import com.flipperplz.bisutils.io.putAsciiZ
import com.flipperplz.bisutils.io.putLong
import com.flipperplz.bisutils.utils.IFlushable
import java.nio.ByteBuffer

interface IMutablePboEntry : IPboEntry, IMutablePboVFSEntry, IFlushable, Cloneable {
    override var parent: IPboDirectory?
    override var node: IPboFile?
    override val absolutePath: String
    override val path: String
    override var entryName: String
    override var entryMime: EntryMimeType
    override var entryDecompressedSize: Long
    override var entryTimestamp: Long
    override var entryOffset: Long
    override var entrySize: Long

    override fun read(buffer: ByteBuffer, options: PboOptions): Boolean {
        entryName = options.entryName ?: buffer.getAsciiZ(options.charset, options.timeoutAsciiSeek)
        entryMime = options.entryMime ?: EntryMimeType.fromMime(buffer.getLong(options.endianness)) ?: return false
        entryDecompressedSize = options.entryOriginalSize ?: buffer.getLong(options.endianness)
        if(options.allowInvalidEntryMeta && entryDecompressedSize < 0) return false
        entryTimestamp = options.entryTimestamp ?: buffer.getLong(options.endianness)
        entryOffset = options.entryOffset ?: buffer.getLong(options.endianness)
        if((options.respectEntryOffsets && options.allowInvalidEntryMeta) && entryOffset <= 0) return false
        entrySize = options.entrySize ?: buffer.getLong(options.endianness)
        return !(options.allowInvalidEntryMeta && entrySize < 0)
    }

    override fun flush() {
        entryOffset = 0; entryMime = EntryMimeType.DUMMY; entryName = ""
        entryDecompressedSize = 0; entrySize = 0; entryTimestamp = 0
    }

    override fun clone(): IMutablePboEntry = super<Cloneable>.clone() as IMutablePboEntry

}