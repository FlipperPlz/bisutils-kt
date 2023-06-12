package com.flipperplz.bisutils.bank.astImpl.entry

import com.flipperplz.bisutils.bank.ast.IPboDirectory
import com.flipperplz.bisutils.bank.ast.IPboFile
import com.flipperplz.bisutils.bank.ast.entry.IPboVersionEntry
import com.flipperplz.bisutils.bank.ast.misc.IPboProperty
import com.flipperplz.bisutils.bank.astImpl.PboEntry
import com.flipperplz.bisutils.bank.utils.EntryMimeType

open class PboVersionEntry(
    parent: IPboDirectory?,
    node: IPboFile?,
    override val entryName: String = "",
    override val entryMime: EntryMimeType = EntryMimeType.VERSION,
    override val entryDecompressedSize: Long = 0,
    override val entryTimestamp: Long = 0,
    override val entryOffset: Long = 0,
    override val entrySize: Long = 0,
    override val familyChildren: List<IPboProperty> = emptyList()
) : PboEntry(parent, node), IPboVersionEntry {
    override val path: String
        get() = super.path
    override val absolutePath: String
        get() = super.absolutePath
}