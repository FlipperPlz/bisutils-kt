package com.flipperplz.bisutils.bank.astImpl.entry.mutable

import com.flipperplz.bisutils.bank.ast.PboFile
import com.flipperplz.bisutils.bank.ast.entry.mutable.MutablePboVersionEntry
import com.flipperplz.bisutils.bank.ast.misc.mutable.MutablePboProperty
import com.flipperplz.bisutils.bank.astImpl.mutable.MutablePboEntryImpl
import com.flipperplz.bisutils.bank.utils.EntryMimeType
import com.flipperplz.bisutils.family.interfaces.FamilyNode

class MutablePboVersionEntryImpl(
    lowestBranch: PboFile?,
    parent: FamilyNode?,
    entryName: String,
    entryMime: EntryMimeType,
    entryDecompressedSize: Long,
    entryOffset: Long,
    entryTimestamp: Long,
    entrySize: Long,
    override var properties: MutableList<MutablePboProperty>,
) : MutablePboEntryImpl(lowestBranch, parent, entryName, entryMime, entryDecompressedSize, entryOffset, entryTimestamp, entrySize),
    MutablePboVersionEntry {

    override val binaryLength: Long
        get() = super<MutablePboVersionEntry>.binaryLength
    }