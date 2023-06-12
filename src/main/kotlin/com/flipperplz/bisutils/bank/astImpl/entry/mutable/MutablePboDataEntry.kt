package com.flipperplz.bisutils.bank.astImpl.entry.mutable

import com.flipperplz.bisutils.bank.ast.IPboDirectory
import com.flipperplz.bisutils.bank.ast.IPboFile
import com.flipperplz.bisutils.bank.ast.entry.mutable.IMutablePboDataEntry
import com.flipperplz.bisutils.bank.astImpl.entry.PboDataEntry
import com.flipperplz.bisutils.bank.utils.EntryMimeType
import java.nio.ByteBuffer

class MutablePboDataEntry(
        override var familyParent: IPboDirectory?,
        override var familyNode: IPboFile?,
        override var entryName: String = "",
        override var entryMime: EntryMimeType = EntryMimeType.DUMMY,
        override var entryDecompressedSize: Long = 0L,
        override var entryTimestamp: Long = 0L,
        override var entryOffset: Long = 0L,
        override var entrySize: Long = 0L,
        override var entryData: ByteBuffer = ByteBuffer.allocate(entrySize.toInt())
) : PboDataEntry(familyParent, familyNode, entryName, entryMime, entryDecompressedSize, entryTimestamp, entryOffset, entrySize, entryData), IMutablePboDataEntry