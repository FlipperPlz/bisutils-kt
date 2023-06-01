package com.flipperplz.bisutils.bank.astImpl.mutable

import PboEntryImpl
import com.flipperplz.bisutils.bank.ast.mutable.MutablePboEntry
import com.flipperplz.bisutils.bank.ast.PboFile
import com.flipperplz.bisutils.bank.utils.EntryMimeType
import com.flipperplz.bisutils.utils.BisFamily

abstract class MutablePboEntryImpl(
    override var lowestBranch: PboFile?,
    override var parent: BisFamily?,
    override var entryName: String,
    override var entryMime: EntryMimeType,
    override var entryDecompressedSize: Long,
    override var entryOffset: Long,
    override var entryTimestamp: Long,
    override var entrySize: Long
) : PboEntryImpl(
    lowestBranch, parent, entryName, entryMime, entryDecompressedSize, entryOffset, entryTimestamp, entrySize
), MutablePboEntry