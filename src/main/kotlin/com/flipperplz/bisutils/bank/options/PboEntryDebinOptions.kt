package com.flipperplz.bisutils.bank.options

import com.flipperplz.bisutils.bank.utils.EntryMimeType
import java.nio.ByteOrder
import java.nio.charset.Charset

class PboEntryDebinOptions(
    charset: Charset = Charsets.UTF_8,
    endianness: ByteOrder = ByteOrder.LITTLE_ENDIAN,
    val entryName: String? = null,
    val entryMime: EntryMimeType? = null,
    val entryOriginalSize: Long? = null,
    val entryOffset: Long? = null,
    val entryTimestamp: Long? = null,
    val entrySize: Long? = null
) : PboDebinarizationOptions()