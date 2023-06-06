package com.flipperplz.bisutils.bank.ast.entry

import com.flipperplz.bisutils.bank.ast.IPboDirectory
import com.flipperplz.bisutils.bank.ast.IPboEntry
import com.flipperplz.bisutils.bank.ast.IPboFile
import com.flipperplz.bisutils.bank.ast.misc.IPboProperty
import com.flipperplz.bisutils.bank.options.PboBinarizationOptions
import com.flipperplz.bisutils.bank.options.PboEntryBinarizationOptions
import com.flipperplz.bisutils.bank.options.PboEntryDebinarizationOptions
import com.flipperplz.bisutils.bank.utils.EntryMimeType
import com.flipperplz.bisutils.binarization.options.DEFAULT_BIS_CHARSET
import com.flipperplz.bisutils.family.IFamilyMember
import com.flipperplz.bisutils.family.IFamilyParent
import com.flipperplz.bisutils.io.putAsciiZ
import java.nio.ByteBuffer
import java.nio.charset.Charset

interface IPboVersionEntry : IPboEntry, IFamilyParent {
    override val parent: IPboDirectory?
    override val node: IPboFile?
    override val path: String
    override val absolutePath: String
    override val entryName: String
    override val entryMime: EntryMimeType
    override val entryDecompressedSize: Long
    override val entryTimestamp: Long
    override val entryOffset: Long
    override val entrySize: Long
    override val children: List<IPboProperty>?

    override fun isValid(): Boolean {
        return entryMime == EntryMimeType.VERSION &&
                entrySize == 0L &&
                entryOffset == 0L &&
                entryName == "" &&
                entryTimestamp == 0L &&
                entryDecompressedSize == 0L &&
                children?.all { it.isValid() } ?: true
    }

    override fun read(buffer: ByteBuffer, options: PboEntryDebinarizationOptions): Boolean =
        throw Exception("Not Supported!")

    override fun calculateBinaryLength(options: PboEntryBinarizationOptions?): Long =
        super.calculateBinaryLength(options) + (children?.sumOf { it.calculateBinaryLength(options) } ?: 0) + 1L

    override fun writeValidated(buffer: ByteBuffer, options: PboEntryBinarizationOptions?): Boolean {
        if(!super.writeValidated(buffer, options)) return false
        children?.forEach {
            buffer.putAsciiZ(it.name, options?.charset ?: DEFAULT_BIS_CHARSET)
            buffer.putAsciiZ(it.value, options?.charset ?: DEFAULT_BIS_CHARSET)
        }
        buffer.put(0)
        return true
    }
}