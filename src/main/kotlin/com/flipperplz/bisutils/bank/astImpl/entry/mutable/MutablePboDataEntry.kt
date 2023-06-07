package com.flipperplz.bisutils.bank.astImpl.entry.mutable

import com.flipperplz.bisutils.bank.ast.entry.mutable.IMutablePboDataEntry
import com.flipperplz.bisutils.bank.ast.mutable.IMutablePboDirectory
import com.flipperplz.bisutils.bank.ast.mutable.IMutablePboFile
import com.flipperplz.bisutils.bank.astImpl.entry.PboDataEntry
import com.flipperplz.bisutils.bank.utils.EntryMimeType
import java.nio.ByteBuffer

class MutablePboDataEntry(
        override var parent: IMutablePboDirectory?,
        override var node: IMutablePboFile?,
        override var entryName: String = "",
        override var entryMime: EntryMimeType = EntryMimeType.DUMMY,
        override var entryDecompressedSize: Long = 0L,
        override var entryTimestamp: Long = 0L,
        override var entryOffset: Long = 0L,
        override var entrySize: Long = 0L,
        override var entryData: ByteBuffer = ByteBuffer.allocate(entrySize.toInt())
) : PboDataEntry(parent, node, entryName, entryMime, entryDecompressedSize, entryTimestamp, entryOffset, entrySize, entryData), IMutablePboDataEntry