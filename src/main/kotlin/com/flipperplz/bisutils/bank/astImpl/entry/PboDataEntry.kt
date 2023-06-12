package com.flipperplz.bisutils.bank.astImpl.entry

import com.flipperplz.bisutils.bank.ast.IPboDirectory
import com.flipperplz.bisutils.bank.ast.IPboFile
import com.flipperplz.bisutils.bank.ast.entry.IPboDataEntry
import com.flipperplz.bisutils.bank.astImpl.PboEntry
import com.flipperplz.bisutils.bank.utils.EntryMimeType
import java.nio.ByteBuffer

open class PboDataEntry(
        override val familyParent: IPboDirectory?,
        override val familyNode: IPboFile?,
        override val entryName: String,
        override val entryMime: EntryMimeType,
        override val entryDecompressedSize: Long,
        override val entryTimestamp: Long,
        override val entryOffset: Long,
        override val entrySize: Long,
        override val entryData: ByteBuffer
) : PboEntry(familyParent, familyNode), IPboDataEntry {
    final override val absolutePath: String
        get() = "${familyParent?.absolutePath}\\${entryName}"
    final override val path: String
        get() = "${familyParent?.path}\\${entryName}"

}