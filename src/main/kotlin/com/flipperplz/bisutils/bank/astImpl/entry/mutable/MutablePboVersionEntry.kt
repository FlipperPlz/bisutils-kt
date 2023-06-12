package com.flipperplz.bisutils.bank.astImpl.entry.mutable

import com.flipperplz.bisutils.bank.ast.IPboDirectory
import com.flipperplz.bisutils.bank.ast.IPboFile
import com.flipperplz.bisutils.bank.ast.entry.mutable.IMutablePboVersionEntry
import com.flipperplz.bisutils.bank.ast.misc.mutable.IMutablePboProperty
import com.flipperplz.bisutils.bank.astImpl.entry.PboVersionEntry
import com.flipperplz.bisutils.bank.utils.EntryMimeType

class MutablePboVersionEntry(
        override var familyParent: IPboDirectory?,
        override var familyNode: IPboFile?,
        override var entryName: String = "",
        override var entryMime: EntryMimeType = EntryMimeType.VERSION,
        override var entryDecompressedSize: Long = 0,
        override var entryTimestamp: Long = 0,
        override var entryOffset: Long = 0,
        override var entrySize: Long = 0,
        override var familyChildren: MutableList<IMutablePboProperty> = mutableListOf()
) : PboVersionEntry(familyParent, familyNode, entryName, entryMime, entryDecompressedSize, entryTimestamp, entryOffset, entrySize, familyChildren), IMutablePboVersionEntry