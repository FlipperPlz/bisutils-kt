package com.flipperplz.bisutils.bank.ast.entry

import com.flipperplz.bisutils.bank.ast.PboEntry
import com.flipperplz.bisutils.bank.ast.misc.PboProperty
import com.flipperplz.bisutils.bank.astImpl.misc.PboPropertyMap
import com.flipperplz.bisutils.bank.utils.EntryMimeType
import com.flipperplz.bisutils.utils.BisFamily
import java.nio.ByteBuffer
import java.nio.charset.Charset

interface PboVersionEntry : PboEntry, List<PboProperty> {
    override val children: List<PboProperty>
        get() = properties.children
    val properties: PboPropertyMap

    override fun isValid(): Boolean = properties.isValid() && entryMime == EntryMimeType.VERSION && entrySize == 0L &&
            entryOffset == 0L && entryName == "" && entryTimestamp == 0L && entryDecompressedSize == 0L

    override val binaryLength: Long
        get() = super.binaryLength + properties.binaryLength

    override fun writeValidated(buffer: ByteBuffer, charset: Charset): Boolean =
        super.writeValidated(buffer, charset) && properties.write(buffer, charset)
}