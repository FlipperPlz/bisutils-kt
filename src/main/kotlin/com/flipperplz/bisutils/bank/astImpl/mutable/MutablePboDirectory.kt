package com.flipperplz.bisutils.bank.astImpl.mutable

import com.flipperplz.bisutils.bank.ast.mutable.IMutablePboDirectory
import com.flipperplz.bisutils.bank.ast.mutable.IMutablePboEntry
import com.flipperplz.bisutils.bank.astImpl.PboDirectory
import com.flipperplz.bisutils.bank.astImpl.PboEntry
import com.flipperplz.bisutils.bank.utils.EntryMimeType
import com.flipperplz.bisutils.family.IFamilyMember
import com.flipperplz.bisutils.family.IFamilyNode

class MutablePboDirectory(
    override var entryName: String,
    override var node: IFamilyNode?,
    override var parent: IFamilyNode?,
    override var children: List<IFamilyMember>?
) : PboDirectory(entryName, node, parent, children), IMutablePboDirectory, IMutablePboEntry {
    override var entryDecompressedSize: Long
        get() = super.entryDecompressedSize
        set(value ) = throw Exception("Not Supported")
    override var entrySize: Long
        get() = super.entrySize
        set(value ) = throw Exception("Not Supported")
    override var entryMime: EntryMimeType
        get() = super.entryMime
        set(value) = throw Exception("Not Supported")
    override var entryOffset: Long
        get() = super.entryOffset
        set(value) =  throw Exception("Not Supported")
    override var entryTimestamp: Long
        get() = super.entryTimestamp
        set(value) = throw Exception("Not Supported")
}