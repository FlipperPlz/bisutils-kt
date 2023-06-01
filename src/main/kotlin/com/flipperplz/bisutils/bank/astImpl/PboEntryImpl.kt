package com.flipperplz.bisutils.bank.astImpl

import com.flipperplz.bisutils.bank.ast.PboEntry
import com.flipperplz.bisutils.bank.utils.EntryMimeType
import com.flipperplz.bisutils.family.interfaces.FamilyNode
import com.flipperplz.bisutils.binarization.BisStrictDebinarizable

abstract class PboEntryImpl(
    override val node: FamilyNode?,
    override val parent: FamilyNode?,
    override val entryName: String,
    override val entryMime: EntryMimeType,
    override val entryDecompressedSize: Long,
    override val entryOffset: Long,
    override val entryTimestamp: Long,
    override val entrySize: Long
) : BisStrictDebinarizable(), PboEntry {

}