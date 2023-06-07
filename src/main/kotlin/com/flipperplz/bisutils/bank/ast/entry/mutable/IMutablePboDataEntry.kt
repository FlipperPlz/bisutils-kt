package com.flipperplz.bisutils.bank.ast.entry.mutable

import com.flipperplz.bisutils.bank.ast.entry.IPboDataEntry
import com.flipperplz.bisutils.bank.ast.mutable.IMutablePboDirectory
import com.flipperplz.bisutils.bank.ast.mutable.IMutablePboEntry
import com.flipperplz.bisutils.bank.ast.mutable.IMutablePboFile
import com.flipperplz.bisutils.bank.options.PboOptions
import com.flipperplz.bisutils.bank.utils.EntryMimeType
import java.nio.ByteBuffer

interface IMutablePboDataEntry : IPboDataEntry, IMutablePboEntry, Cloneable {
    override val absolutePath: String
    override var entryDecompressedSize: Long
    override var entryMime: EntryMimeType
    override var entryName: String
    override var entryOffset: Long
    override var entrySize: Long
    override var entryTimestamp: Long
    override var node: IMutablePboFile?
    override var parent: IMutablePboDirectory?
    override var entryData: ByteBuffer
    override var path: String

    override fun validateMutability(): Boolean = !super.validateMutability()

    override fun read(buffer: ByteBuffer, options: PboOptions): Boolean =
        super<IMutablePboEntry>.read(buffer, options)

    override fun clone(): IMutablePboDataEntry = super<Cloneable>.clone() as IMutablePboDataEntry
}