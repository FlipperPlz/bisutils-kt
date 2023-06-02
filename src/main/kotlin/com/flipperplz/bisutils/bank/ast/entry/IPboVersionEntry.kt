package com.flipperplz.bisutils.bank.ast.entry

import com.flipperplz.bisutils.bank.ast.IPboEntry
import com.flipperplz.bisutils.bank.ast.misc.IPboProperty
import com.flipperplz.bisutils.bank.utils.EntryMimeType
import com.flipperplz.bisutils.family.interfaces.IFamilyMember
import com.flipperplz.bisutils.family.interfaces.IFamilyParent
import com.flipperplz.bisutils.io.putAsciiZ
import java.nio.ByteBuffer
import java.nio.charset.Charset

interface IPboVersionEntry : IPboEntry, IFamilyParent {
    val properties: List<IPboProperty>
    override val children: List<IFamilyMember>?
        get() = properties

    override fun isValid(): Boolean =entryMime == EntryMimeType.VERSION && entrySize == 0L &&
            entryOffset == 0L && entryName == "" && entryTimestamp == 0L && entryDecompressedSize == 0L

    override val binaryLength: Long
        get() = super.binaryLength + properties.sumOf { it.binaryLength } + 1L

    override fun writeValidated(buffer: ByteBuffer, charset: Charset): Boolean {
        if(!super.writeValidated(buffer, charset)) return false
        properties.forEach {
            buffer.putAsciiZ(it.name, charset)
            buffer.putAsciiZ(it.value, charset)
        }
        buffer.put(0)
        return true
    }
}