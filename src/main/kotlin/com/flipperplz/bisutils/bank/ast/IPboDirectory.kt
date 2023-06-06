package com.flipperplz.bisutils.bank.ast

import com.flipperplz.bisutils.bank.options.PboEntryBinarizationOptions
import com.flipperplz.bisutils.bank.options.PboEntryDebinarizationOptions
import com.flipperplz.bisutils.family.IFamilyParent
import java.nio.ByteBuffer

interface IPboDirectory : IFamilyParent, IPboVFSEntry {
    override val entryName: String
    override val children: List<IPboVFSEntry>?
    override val node: IPboFile?
    override val parent: IPboDirectory?
    override val absolutePath: String get() = super.absolutePath
    override val path: String get() = super.path

    val directories: List<IPboDirectory> get() = children?.filterIsInstance<IPboDirectory>() ?: emptyList()
    val entries: List<IPboEntry> get() = children?.filterIsInstance<IPboEntry>() ?: emptyList()

    override fun calculateBinaryLength(options: PboEntryBinarizationOptions?): Long =
        children?.sumOf { it.calculateBinaryLength(options) } ?: 0

    override fun read(buffer: ByteBuffer, options: PboEntryDebinarizationOptions): Boolean =
        throw Exception("Not Supported")

    override fun isValid(): Boolean {
        children?.forEach { if (!it.isValid()) return false }
        return true
    }

    override fun writeValidated(buffer: ByteBuffer, options: PboEntryBinarizationOptions?): Boolean {
        entries.forEach { if(!it.writeValidated(buffer, options)) return false }
        directories.forEach { if(!it.writeValidated(buffer, options)) return false }
        return true
    }
}