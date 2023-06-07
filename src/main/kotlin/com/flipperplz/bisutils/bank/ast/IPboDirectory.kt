package com.flipperplz.bisutils.bank.ast

import com.flipperplz.bisutils.bank.options.PboOptions
import com.flipperplz.bisutils.family.IFamilyParent
import com.flipperplz.bisutils.options.IOptions
import java.nio.ByteBuffer

interface IPboDirectory : IFamilyParent, IPboVFSEntry, Cloneable {
    override val entryName: String
    override val children: List<IPboVFSEntry>?
    override val node: IPboFile?
    override val parent: IPboDirectory?
    override val absolutePath: String get() = super.absolutePath
    override val path: String get() = super.path

    val directories: List<IPboDirectory> get() = children?.filterIsInstance<IPboDirectory>() ?: emptyList()
    val entries: List<IPboEntry> get() = children?.filterIsInstance<IPboEntry>() ?: emptyList()

    override fun calculateBinaryLength(options: PboOptions?): Long =
        children?.sumOf { it.calculateBinaryLength(options) } ?: 0

    override fun read(buffer: ByteBuffer, options: PboOptions): Boolean =
        throw Exception("Not Supported!")

    override fun isValid(options: IOptions?): Boolean {
        children?.forEach { if (!it.isValid(options)) return false }
        return true
    }

    override fun writeValidated(buffer: ByteBuffer, options: PboOptions?): Boolean {
        entries.forEach { if(!it.writeValidated(buffer, options)) return false }
        directories.forEach { if(!it.writeValidated(buffer, options)) return false }
        return true
    }

    public override fun clone(): IPboDirectory = super<Cloneable>.clone() as IPboDirectory
}