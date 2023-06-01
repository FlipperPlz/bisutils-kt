package com.flipperplz.bisutils.bank.astImpl.entry

import PboEntryImpl
import com.flipperplz.bisutils.bank.ast.PboFile
import com.flipperplz.bisutils.bank.ast.entry.PboVersionEntry
import com.flipperplz.bisutils.bank.ast.misc.PboProperty
import com.flipperplz.bisutils.bank.astImpl.misc.PboPropertyMap
import com.flipperplz.bisutils.bank.utils.EntryMimeType
import com.flipperplz.bisutils.utils.BisFamily

class PboVersionEntryImpl(
    lowestBranch: PboFile?,
    parent: BisFamily?, entryName: String, entryMime: EntryMimeType,
    entryDecompressedSize: Long,
    entryOffset: Long,
    entryTimestamp: Long,
    entrySize: Long,
    override val propertiesMap: PboPropertyMap
) : PboEntryImpl(lowestBranch, parent, entryName, entryMime, entryDecompressedSize, entryOffset, entryTimestamp, entrySize),
    PboVersionEntry,

