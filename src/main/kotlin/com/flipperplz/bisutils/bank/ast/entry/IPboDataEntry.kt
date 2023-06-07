package com.flipperplz.bisutils.bank.ast.entry

import com.flipperplz.bisutils.bank.ast.IPboDirectory
import com.flipperplz.bisutils.bank.ast.IPboEntry
import com.flipperplz.bisutils.bank.ast.IPboFile
import com.flipperplz.bisutils.bank.options.PboOptions
import com.flipperplz.bisutils.bank.utils.EntryMimeType
import com.flipperplz.bisutils.options.IOptions
import java.nio.ByteBuffer

interface IPboDataEntry : IPboEntry, Cloneable {
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

    override fun isValid(options: IOptions?): Boolean =
        entrySize == entryData.capacity().toLong() && validateMutability()

    fun validateMutability(): Boolean = entryData.isReadOnly

    override fun read(buffer: ByteBuffer, options: PboOptions): Boolean =
        throw Exception("Not Supported!")

    override fun writeValidated(buffer: ByteBuffer, options: PboOptions?): Boolean = super.writeValidated(buffer, options)

    public override fun clone(): IPboDataEntry =  super<Cloneable>.clone() as IPboDataEntry
}