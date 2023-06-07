package com.flipperplz.bisutils.bank.astImpl.entry.mutable

import com.flipperplz.bisutils.bank.ast.entry.mutable.IMutablePboVersionEntry
import com.flipperplz.bisutils.bank.ast.misc.mutable.IMutablePboProperty
import com.flipperplz.bisutils.bank.ast.mutable.IMutablePboDirectory
import com.flipperplz.bisutils.bank.ast.mutable.IMutablePboFile
import com.flipperplz.bisutils.bank.astImpl.entry.PboVersionEntry
import com.flipperplz.bisutils.bank.utils.EntryMimeType

class MutablePboVersionEntry(
    override var parent: IMutablePboDirectory?,
    override var node: IMutablePboFile?,
    override var entryName: String = "",
    override var entryMime: EntryMimeType = EntryMimeType.VERSION,
    override var entryDecompressedSize: Long = 0,
    override var entryTimestamp: Long = 0,
    override var entryOffset: Long = 0,
    override var entrySize: Long = 0,
    override var children: MutableList<IMutablePboProperty> = mutableListOf()
) : PboVersionEntry(parent, node, entryName, entryMime, entryDecompressedSize, entryTimestamp, entryOffset, entrySize, children), IMutablePboVersionEntry