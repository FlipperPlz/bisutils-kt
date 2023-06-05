package com.flipperplz.bisutils.bank.astImpl

import com.flipperplz.bisutils.bank.ast.IPboDirectory
import com.flipperplz.bisutils.bank.ast.IPboEntry
import com.flipperplz.bisutils.bank.options.PboBinarizationOptions
import com.flipperplz.bisutils.bank.options.PboDebinarizationOptions
import com.flipperplz.bisutils.bank.utils.EntryMimeType
import com.flipperplz.bisutils.bank.utils.IPboBinaryObject
import com.flipperplz.bisutils.binarization.BisBinaryObject
import com.flipperplz.bisutils.family.IFamilyMember
import com.flipperplz.bisutils.family.IFamilyNode
import com.flipperplz.bisutils.utils.IValidatable
import java.nio.ByteBuffer
import java.nio.charset.Charset

open class PboDirectory(
    override val entryName: String,
    override val node: IFamilyNode?,
    override val parent: IFamilyNode?,
    override val children: List<IFamilyMember>?
) : IPboBinaryObject, IPboDirectory {
    override val entryDecompressedSize: Long
        get() = children?.filterIsInstance<PboEntry>()?.sumOf { it.entryDecompressedSize } ?: 0
    override val entrySize: Long
        get() = children?.filterIsInstance<PboEntry>()?.sumOf { it.entryDecompressedSize } ?: 0
    override val entryMime: EntryMimeType = EntryMimeType.DUMMY
    override val entryTimestamp: Long = 0
    override val entryOffset: Long = 0
    override fun isValid(): Boolean = children?.filterIsInstance<IValidatable>()?.all { it.isValid() } ?: true
    override fun read(buffer: ByteBuffer, charset: Charset, options: PboDebinarizationOptions): Boolean =
        throw Exception("Cannot read psuedo-structures.")

    override fun writeValidated(buffer: ByteBuffer, charset: Charset, options: PboBinarizationOptions?): Boolean  =
        children?.filterIsInstance<IPboEntry>()?.all { it.writeValidated(buffer, charset, options) } ?: true
}