package com.flipperplz.bisutils.bank.ast.entry.mutable

import com.flipperplz.bisutils.bank.ast.entry.IPboVersionEntry
import com.flipperplz.bisutils.bank.ast.misc.IPboProperty
import com.flipperplz.bisutils.bank.ast.misc.mutable.IMutablePboProperty
import com.flipperplz.bisutils.bank.ast.mutable.IMutablePboDirectory
import com.flipperplz.bisutils.bank.ast.mutable.IMutablePboEntry
import com.flipperplz.bisutils.bank.ast.mutable.IMutablePboFile
import com.flipperplz.bisutils.bank.options.PboEntryDebinarizationOptions
import com.flipperplz.bisutils.bank.utils.EntryMimeType
import java.nio.ByteBuffer

interface IMutablePboVersionEntry : IPboVersionEntry, IMutablePboEntry {
    override val absolutePath: String
    override val path: String
    override var children: MutableList<IMutablePboProperty>
    override var entryDecompressedSize: Long
    override var entryMime: EntryMimeType
    override var entryName: String
    override var entryOffset: Long
    override var entrySize: Long
    override var entryTimestamp: Long
    override var node: IMutablePboFile?
    override var parent: IMutablePboDirectory?
    override fun flush() { super.flush(); children.clear() }

    override fun read(buffer: ByteBuffer, options: PboEntryDebinarizationOptions): Boolean {
        if(!super<IMutablePboEntry>.read(buffer, options)) return false

        //TODO: Read Properties
        return true
    }
}