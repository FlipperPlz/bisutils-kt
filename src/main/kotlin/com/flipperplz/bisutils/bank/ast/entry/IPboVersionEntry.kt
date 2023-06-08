package com.flipperplz.bisutils.bank.ast.entry

import com.flipperplz.bisutils.bank.ast.IPboDirectory
import com.flipperplz.bisutils.bank.ast.IPboEntry
import com.flipperplz.bisutils.bank.ast.IPboFile
import com.flipperplz.bisutils.bank.ast.misc.IPboProperty
import com.flipperplz.bisutils.bank.options.PboOptions
import com.flipperplz.bisutils.bank.utils.EntryMimeType
import com.flipperplz.bisutils.family.IFamilyParent
import com.flipperplz.bisutils.options.IOptions
import java.nio.ByteBuffer

interface IPboVersionEntry : IPboEntry, IFamilyParent, Cloneable {
    override val parent: IPboDirectory?
    override val node: IPboFile?
    override val path: String
        get() = parent?.path ?: ""
    override val absolutePath: String
        get() = parent?.absolutePath ?: ""

    override val entryName: String
    override val entryMime: EntryMimeType
    override val entryDecompressedSize: Long
    override val entryTimestamp: Long
    override val entryOffset: Long
    override val entrySize: Long
    override val children: List<IPboProperty>

    override fun isValid(options: IOptions?): Boolean = entryMime == EntryMimeType.VERSION &&
        entrySize == 0L &&
        entryOffset == 0L &&
        entryName == "" &&
        entryTimestamp == 0L &&
        entryDecompressedSize == 0L &&
        children.all { it.isValid(options) }

    override fun read(buffer: ByteBuffer, options: PboOptions): Boolean =
        throw Exception("Not Supported!")

    override fun calculateBinaryLength(options: PboOptions?): Long =
        super.calculateBinaryLength(options) + (children.sumOf { it.calculateBinaryLength(options) } ?: 0) + 1L

    override fun writeValidated(buffer: ByteBuffer, options: PboOptions?): Boolean {
        if(!super.writeValidated(buffer, options)) return false
        children.forEach { it.writeValidated(buffer, options) }
        buffer.put(0)
        return true
    }

    public override fun clone(): IPboVersionEntry =  super<Cloneable>.clone() as IPboVersionEntry

}