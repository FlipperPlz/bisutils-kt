package com.flipperplz.bisutils.bank.options

import com.flipperplz.bisutils.bank.utils.EntryMimeType
import java.nio.ByteOrder
import java.nio.charset.Charset

interface PboEntryOptions {
    val entryName: String?
    val entryMime: EntryMimeType?
    val entryOriginalSize: Long?
    val entryOffset: Long?
    val entryTimestamp: Long?
    val entrySize: Long?
}

class PboEntryBinarizationOptions(
    charset: Charset = Charsets.UTF_8,
    endianness: ByteOrder = ByteOrder.LITTLE_ENDIAN,
    skipValidation: Boolean = false,
    override val entryName: String? = null,
    override val entryMime: EntryMimeType? = null,
    override val entryOriginalSize: Long? = null,
    override val entryOffset: Long? = null,
    override val entryTimestamp: Long? = null,
    override val entrySize: Long? = null,
): PboBinarizationOptions(charset, endianness, skipValidation), PboEntryOptions

class PboEntryDebinarizationOptions(
    charset: Charset = Charsets.UTF_8,
    endianness: ByteOrder = ByteOrder.LITTLE_ENDIAN,
    override val entryName: String? = null,
    override val entryMime: EntryMimeType? = null,
    override val entryOriginalSize: Long? = null,
    override val entryOffset: Long? = null,
    override val entryTimestamp: Long? = null,
    override val entrySize: Long? = null,
): PboDebinarizationOptions(charset, endianness), PboEntryOptions
