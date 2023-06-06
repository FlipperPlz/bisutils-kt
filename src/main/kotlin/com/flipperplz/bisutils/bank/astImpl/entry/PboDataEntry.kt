package com.flipperplz.bisutils.bank.astImpl.entry

import com.flipperplz.bisutils.bank.ast.IPboDirectory
import com.flipperplz.bisutils.bank.ast.IPboFile
import com.flipperplz.bisutils.bank.ast.entry.IPboDataEntry
import com.flipperplz.bisutils.bank.astImpl.PboEntry
import com.flipperplz.bisutils.bank.utils.EntryMimeType
import java.nio.ByteBuffer

open class PboDataEntry(
    override val parent: IPboDirectory?,
    override val node: IPboFile?,
    override val entryName: String,
    override val entryMime: EntryMimeType,
    override val entryDecompressedSize: Long,
    override val entryTimestamp: Long,
    override val entryOffset: Long,
    override val entrySize: Long,
    override val entryData: ByteBuffer
) : PboEntry(parent, node), IPboDataEntry {
    final override val absolutePath: String
        get() = "${parent?.absolutePath}\\${entryName}"
    final override val path: String
        get() = "${parent?.path}\\${entryName}"

}