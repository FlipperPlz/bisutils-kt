package com.flipperplz.bisutils.bank.ast.mutable

import com.flipperplz.bisutils.bank.ast.IPboFile
import com.flipperplz.bisutils.bank.astImpl.entry.mutable.MutablePboDataEntry
import com.flipperplz.bisutils.bank.astImpl.entry.mutable.MutablePboVersionEntry
import com.flipperplz.bisutils.bank.options.PboOptions
import com.flipperplz.bisutils.bank.utils.EntryMimeType
import com.flipperplz.bisutils.binarization.options.IBinarizationOptions.Companion.DEFAULT_BIS_CHARSET
import com.flipperplz.bisutils.binarization.options.IBinarizationOptions.Companion.DEFAULT_BIS_ENDIANNESS
import com.flipperplz.bisutils.io.getAsciiZ
import com.flipperplz.bisutils.io.getLong
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
    override fun read(buffer: ByteBuffer, options: PboOptions): Boolean {

        var i = 0
        var versionEntryEncountered = false
        while (true.also { i++ }) {

            options.entryName = buffer.getAsciiZ(options.charset ?: DEFAULT_BIS_CHARSET)
            options.entryMime = EntryMimeType.fromMime(buffer.getLong(options.endianness ?: DEFAULT_BIS_ENDIANNESS)) ?: return false
            options.entryOriginalSize = buffer.getLong(options.endianness ?: DEFAULT_BIS_ENDIANNESS)
            options.entryTimestamp = buffer.getLong(options.endianness ?: DEFAULT_BIS_ENDIANNESS)
            options.entryOffset = buffer.getLong(options.endianness ?: DEFAULT_BIS_ENDIANNESS)
            options.entrySize = buffer.getLong(options.endianness ?: DEFAULT_BIS_ENDIANNESS)

            if(
                options.entryName == "" && options.entryMime == EntryMimeType.DUMMY &&
                options.entryOriginalSize == 0L && options.entryTimestamp == 0L && 
                options.entryOffset == 0L && options.entrySize == 0L
            ) {
                if(options.emptyIsAlwaysSeparator) break
                //TODO: Try recover
            }
            when(options.entryMime) {
                EntryMimeType.VERSION -> {
                    if(versionEntryEncountered && options.allowMultipleVersionEntries)
                        throw Exception()//TODO(exception): multiple version entries encountered

                    val entry = MutablePboVersionEntry(this@IMutablePboFile, this@IMutablePboFile)
                    if(!entry.read(buffer, options)) return false

                    versionEntryEncountered = true
                    children?.add(entry)
                }
                else -> {
                    if(i == 1 && options.requireVersionEntryFirst) throw Exception()//TODO(exception): First entry should be version
                    //TODO: Normalize and disassociate to directories
                    //children?.add(MutablePboDataEntry(this@IMutablePboFile, this@IMutablePboFile, name, mime, ogSize, timestamp, offset, size, ByteBuffer.allocate(size.toInt())))
                }
            }


        }

        return true
    }

    override fun clone(): IMutablePboFile = super<Cloneable>.clone() as IMutablePboFile

}