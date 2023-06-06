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
    override var entryName: String,
    override var entryMime: EntryMimeType,
    override var entryDecompressedSize: Long,
    override var entryTimestamp: Long,
    override var entryOffset: Long,
    override var entrySize: Long,
    override var entryData: ByteBuffer
) : PboDataEntry(parent, node, entryName, entryMime, entryDecompressedSize, entryTimestamp, entryOffset, entrySize, entryData), IMutablePboDataEntry