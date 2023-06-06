package com.flipperplz.bisutils.bank.ast.entry

import com.flipperplz.bisutils.bank.ast.IPboDirectory
import com.flipperplz.bisutils.bank.ast.IPboEntry
import com.flipperplz.bisutils.bank.ast.IPboFile
import com.flipperplz.bisutils.bank.options.PboEntryBinarizationOptions
import com.flipperplz.bisutils.bank.options.PboEntryDebinarizationOptions
import com.flipperplz.bisutils.bank.utils.EntryMimeType
import com.flipperplz.bisutils.options.BisOptions
import java.nio.ByteBuffer

interface IPboDataEntry : IPboEntry {
    override val node: IPboFile?
    override val parent: IPboDirectory?
    override val entryName: String
    override val entryMime: EntryMimeType
    override val entryDecompressedSize: Long
    override val entryTimestamp: Long
    override val entryOffset: Long
    override val entrySize: Long
    override val absolutePath: String
    override val path: String
    val entryData: ByteBuffer

    override fun isValid(options: BisOptions?): Boolean =
        entrySize == entryData.capacity().toLong() && validateMutability()

    fun validateMutability(): Boolean = entryData.isReadOnly

    override fun read(buffer: ByteBuffer, options: PboEntryDebinarizationOptions): Boolean =
        throw Exception("Not Supported!")

    override fun writeValidated(buffer: ByteBuffer, options: PboEntryBinarizationOptions?): Boolean = super.writeValidated(buffer, options)
}