package com.flipperplz.bisutils.bank.astImpl.entry.mutable

import com.flipperplz.bisutils.bank.ast.PboFile
import com.flipperplz.bisutils.bank.ast.entry.PboVersionEntry
import com.flipperplz.bisutils.bank.ast.entry.mutable.MutablePboVersionEntry
import com.flipperplz.bisutils.bank.ast.mutable.MutablePboEntry
import com.flipperplz.bisutils.bank.astImpl.misc.PboPropertyMap
import com.flipperplz.bisutils.bank.astImpl.misc.mutable.MutablePboPropertyMap
import com.flipperplz.bisutils.bank.astImpl.mutable.MutablePboEntryImpl
import com.flipperplz.bisutils.bank.utils.EntryMimeType
import com.flipperplz.bisutils.utils.BisFamily

class MutablePboVersionEntryImpl(
    lowestBranch: PboFile?,
    parent: BisFamily?,
    entryName: String,
    entryMime: EntryMimeType,
    entryDecompressedSize: Long,
    entryOffset: Long,
    entryTimestamp: Long,
    entrySize: Long,
    override val propertiesMap: MutablePboPropertyMap
) : MutablePboEntryImpl(lowestBranch, parent, entryName, entryMime, entryDecompressedSize, entryOffset, entryTimestamp, entrySize),
    MutablePboVersionEntry