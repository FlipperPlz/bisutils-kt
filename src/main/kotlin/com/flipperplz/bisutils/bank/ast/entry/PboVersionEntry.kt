package com.flipperplz.bisutils.bank.ast.entry

import com.flipperplz.bisutils.bank.ast.PboEntry
import com.flipperplz.bisutils.bank.ast.misc.PboProperty
import com.flipperplz.bisutils.bank.utils.EntryMimeType
import com.flipperplz.bisutils.family.interfaces.FamilyMember
import com.flipperplz.bisutils.family.interfaces.FamilyParent
import com.flipperplz.bisutils.io.putAsciiZ
import java.nio.ByteBuffer
import java.nio.charset.Charset

interface PboVersionEntry : PboEntry, FamilyParent {
    val properties: List<PboProperty>
    override val children: List<FamilyMember>?
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