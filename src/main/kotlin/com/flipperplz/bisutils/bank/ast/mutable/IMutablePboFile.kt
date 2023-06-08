package com.flipperplz.bisutils.bank.ast.mutable

import com.flipperplz.bisutils.bank.ast.IPboDirectory
import com.flipperplz.bisutils.bank.ast.IPboFile
import com.flipperplz.bisutils.bank.astImpl.entry.mutable.MutablePboDataEntry
import com.flipperplz.bisutils.bank.astImpl.entry.mutable.MutablePboVersionEntry
import com.flipperplz.bisutils.bank.options.PboOptions
import com.flipperplz.bisutils.bank.utils.BankPathUtils.getFilename
import com.flipperplz.bisutils.bank.utils.BankPathUtils.getParent
import com.flipperplz.bisutils.bank.utils.BankPathUtils.normalizePboPath
import com.flipperplz.bisutils.bank.utils.EntryMimeType
import com.flipperplz.bisutils.io.getAsciiZ
import com.flipperplz.bisutils.io.getLong
import java.nio.ByteBuffer

interface IMutablePboFile : IPboFile, IMutablePboDirectory, Cloneable {
    override var parent: IPboDirectory?
    override var node: IPboFile?
    override var defaultPrefix: String
    override var signature: ByteArray
    override val children: MutableList<IMutablePboVFSEntry>
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

            options.entryName = buffer.getAsciiZ(options.charset)
            options.entryMime = EntryMimeType.fromMime(buffer.getLong(options.endianness)) ?: return false
            options.entryOriginalSize = buffer.getLong(options.endianness)
            options.entryTimestamp = buffer.getLong(options.endianness)
            options.entryOffset = buffer.getLong(options.endianness)
            options.entrySize = buffer.getLong(options.endianness)

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
                    if(options.requireBlankVersionMeta &&
                       (options.entryName != "" ||
                           options.entryOriginalSize != 0L ||
                           options.entryTimestamp != 0L ||
                           options.entryOffset == 0L ||
                           options.entrySize == 0L
                       )
                      ) throw Exception("TODO non blank version entry encountered")
                    if(versionEntryEncountered && options.allowMultipleVersionEntries)
                        throw Exception("TODO multiple version entries encountered")

                    val entry = MutablePboVersionEntry(this@IMutablePboFile, this@IMutablePboFile)
                    if(!entry.read(buffer, options)) return false

                    versionEntryEncountered = true
                    children.add(entry)
                }
                else -> {
                    if(i == 1 && options.requireVersionEntryFirst) throw Exception("First entry should be version (requireVersionEntryFirst is enabled)")
                    val path = normalizePboPath(options.entryName!!)
                    val parent = createDirectory(getParent(path))
                    children.add(parent)
                    options.entryName = getFilename(path)
                    parent.children.add(MutablePboDataEntry(parent, node).apply { read(buffer, options) })
                }
            }
        }

        return true
    }

    override fun clone(): IMutablePboFile = super<Cloneable>.clone() as IMutablePboFile

}