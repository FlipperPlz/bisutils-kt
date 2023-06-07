package com.flipperplz.bisutils.bank.ast.mutable

import com.flipperplz.bisutils.bank.ast.IPboFile
import com.flipperplz.bisutils.bank.ast.entry.mutable.IMutablePboDataEntry
import com.flipperplz.bisutils.bank.astImpl.entry.PboVersionEntry
import com.flipperplz.bisutils.bank.astImpl.entry.mutable.MutablePboDataEntry
import com.flipperplz.bisutils.bank.astImpl.entry.mutable.MutablePboVersionEntry
import com.flipperplz.bisutils.bank.options.PboEntryDebinarizationOptions
import com.flipperplz.bisutils.bank.utils.EntryMimeType
import com.flipperplz.bisutils.binarization.options.DEFAULT_BIS_CHARSET
import com.flipperplz.bisutils.binarization.options.DEFAULT_BIS_ENDIANNESS
import com.flipperplz.bisutils.io.getAsciiZ
import com.flipperplz.bisutils.io.getLong
import com.flipperplz.bisutils.options.BisOptions
import java.nio.ByteBuffer

interface IMutablePboFile : IPboFile, IMutablePboDirectory, Cloneable {
    override var parent: IMutablePboDirectory?
    override var node: IMutablePboFile?
    override var defaultPrefix: String
    override var signature: ByteArray
    override val children: MutableList<IMutablePboVFSEntry>?
    override val absolutePath: String get() = super<IMutablePboDirectory>.absolutePath
    override val path: String get() = super<IMutablePboDirectory>.path

    override var entryName: String
        get() = defaultPrefix
        set(value)  { defaultPrefix = value }//TODO Allow version property change if available

    override var directories: List<IMutablePboDirectory>
        get() = super<IMutablePboDirectory>.directories
        set(value) { super<IMutablePboDirectory>.directories = value }

    override var entries: List<IMutablePboEntry>
        get() = super<IMutablePboDirectory>.entries
        set(value) { super<IMutablePboDirectory>.entries = value }

    //TODO: READ
    override fun read(buffer: ByteBuffer, options: PboEntryDebinarizationOptions): Boolean {

        while (true) {
            val name = buffer.getAsciiZ(options.charset ?: DEFAULT_BIS_CHARSET)
            val mime = EntryMimeType.fromMime(buffer.getLong(options.endianness ?: DEFAULT_BIS_ENDIANNESS)) ?: return false
            val ogSize = buffer.getLong(options.endianness ?: DEFAULT_BIS_ENDIANNESS)
            val timestamp = buffer.getLong(options.endianness ?: DEFAULT_BIS_ENDIANNESS)
            val offset = buffer.getLong(options.endianness ?: DEFAULT_BIS_ENDIANNESS)
            val size = buffer.getLong(options.endianness ?: DEFAULT_BIS_ENDIANNESS)
            if(
                name == "" &&
                mime == EntryMimeType.DUMMY &&
                ogSize == 0L &&
                timestamp == 0L &&
                offset == 0L &&
                size == 0L
            ) break
            with(PboEntryDebinarizationOptions(
                    entryName = name,
                    entryMime = mime,
                    entryOriginalSize = ogSize,
                    entryTimestamp = timestamp,
                    entryOffset = offset,
                    entrySize = size
            )) {
                when(mime) {
                    EntryMimeType.VERSION -> {
                        val entry = MutablePboVersionEntry(this@IMutablePboFile, this@IMutablePboFile)
                        if(!entry.read(buffer, this)) return false
                        children?.add(entry)
                    }
                    else -> {
                        //TODO: Normalize and disassociate to directories
                        children?.add(MutablePboDataEntry(this@IMutablePboFile, this@IMutablePboFile, name, mime, ogSize, timestamp, offset, size, ByteBuffer.allocate(size.toInt())))
                    }
                }
            }


        }

        return true
    }

    override fun clone(): IMutablePboFile = super<Cloneable>.clone() as IMutablePboFile

}