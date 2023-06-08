package com.flipperplz.bisutils.bank.options

import com.flipperplz.bisutils.bank.utils.EntryMimeType
import com.flipperplz.bisutils.binarization.options.IBinarizationOptions
import java.nio.ByteOrder
import java.nio.charset.Charset

class PboOptions(
    override val charset: Charset = Charsets.UTF_8,
    override val endianness: ByteOrder = ByteOrder.LITTLE_ENDIAN,
    override val skipValidation: Boolean = false,
    var skipDataRead: Boolean = false,
    var timeoutAsciiSeek: Int? = null, //Assume read fuck up if string is this long
    var timeoutDecompression: Int = 1, //minutes
    var respectEntryOffsets: Boolean = false, // Some of the Arma games use said offsets
    var requireVersionEntryFirst: Boolean = true,
    var emptyIsAlwaysSeparator: Boolean = true,
    var requireDummySeparator: Boolean = true,
    var requireBlankVersionMeta: Boolean = true,
    var requireValidSignature: Boolean = true,
    var allowMultipleVersionEntries: Boolean = false,
    var allowCompressionErrors: Boolean = false,
    var allowUnnamedEntries: Boolean = false,
    var allowObfuscatedBank: Boolean = false,
    var allowFPackerBank: Boolean = false,
    var allowEncryptedBank: Boolean = false,
    var allowInvalidEntryMeta: Boolean = false,
    var allowDuplicateFiles: Boolean = false,
    var registerEmptyFiles: Boolean = false,
    var removeBenignProperties: Boolean = true,
    var entryName: String?,
    var entryMime: EntryMimeType?,
    var entryOriginalSize: Long?,
    var entryOffset: Long?,
    var entryTimestamp: Long?,
    var entrySize: Long?,
) : IBinarizationOptions