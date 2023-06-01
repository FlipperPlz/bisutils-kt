package com.flipperplz.bisutils.bank.ast

import com.flipperplz.bisutils.bank.ast.misc.PboElement
import com.flipperplz.bisutils.bank.utils.EntryMimeType
import com.flipperplz.bisutils.utils.BisStrictBinarizable
import com.flipperplz.bisutils.utils.putAsciiZ
import java.nio.ByteBuffer
import java.nio.charset.Charset

interface PboEntry : BisStrictBinarizable, PboElement {
    val entryName: String
    val entryMime: EntryMimeType
    val entryTimestamp: Long
    val entryOffset: Long
    val entryDecompressedSize: Long
    val entrySize: Long

    override fun writeValidated(buffer: ByteBuffer, charset: Charset): Boolean {
        buffer.putAsciiZ(entryName, charset)
        if(!entryMime.write(buffer, charset)) return false
        buffer.putLong(entryDecompressedSize)
        buffer.putLong(entryOffset)
        buffer.putLong(entryTimestamp)
        buffer.putLong(entrySize)
        return true
    }

    override val binaryLength: Long
        get() = 21L + entryName.length
}