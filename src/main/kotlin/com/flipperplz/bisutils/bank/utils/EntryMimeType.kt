package com.flipperplz.bisutils.bank.utils

import com.flipperplz.bisutils.bank.options.PboOptions
import com.flipperplz.bisutils.binarization.interfaces.IBinarizable
import com.flipperplz.bisutils.binarization.options.IBinarizationOptions.Companion.DEFAULT_BIS_ENDIANNESS
import com.flipperplz.bisutils.io.putLong

import java.nio.ByteBuffer

enum class EntryMimeType(
    val mime: Long
) : IBinarizable<PboOptions> {
    VERSION(0x56657273),
    NORMAL_DATA(0x43707273),
    ENCRYPTED_DATA(0x456e6372),
    DUMMY(0x00000000);

    override fun calculateBinaryLength(options: PboOptions?): Long = 4
    override fun write(buffer: ByteBuffer, options: PboOptions?): Boolean {
        buffer.putLong(mime, options?.endianness ?: DEFAULT_BIS_ENDIANNESS)
        return true
    }

    companion object {
        fun fromMime(mime: Long): EntryMimeType? =
            values().firstOrNull { it.mime == mime }
    }


}