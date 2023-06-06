package com.flipperplz.bisutils.bank.utils

import com.flipperplz.bisutils.bank.options.PboBinarizationOptions
import com.flipperplz.bisutils.bank.options.PboDebinarizationOptions
import com.flipperplz.bisutils.bank.options.PboEntryBinarizationOptions
import com.flipperplz.bisutils.binarization.interfaces.IBinarizable
import com.flipperplz.bisutils.binarization.options.DEFAULT_BIS_ENDIANNESS
import com.flipperplz.bisutils.io.putLong

import java.nio.ByteBuffer

enum class EntryMimeType(
    val mime: Long
) : IBinarizable<PboEntryBinarizationOptions> {
    VERSION(0x56657273),
    NORMAL_DATA(0x43707273),
    ENCRYPTED_DATA(0x456e6372),
    DUMMY(0x00000000);

    override fun calculateBinaryLength(options: PboEntryBinarizationOptions?): Long = 4
    override fun write(buffer: ByteBuffer, options: PboEntryBinarizationOptions?): Boolean {
        buffer.putLong(mime, options?.endianness ?: DEFAULT_BIS_ENDIANNESS)
        return true
    }

    companion object {

        fun fromMime(mime: Long): EntryMimeType? =
            values().firstOrNull { it.mime == mime }
    }


}