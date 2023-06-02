package com.flipperplz.bisutils.bank.utils

import com.flipperplz.bisutils.bank.options.PboBinarizationOptions
import com.flipperplz.bisutils.bank.options.PboDebinarizationOptions
import com.flipperplz.bisutils.binarization.interfaces.IBinarizable
import java.nio.ByteBuffer
import java.nio.charset.Charset

enum class EntryMimeType(
    val mime: Int
) : IBinarizable<PboBinarizationOptions> {
    VERSION(0x56657273),
    NORMAL_DATA(0x43707273),
    ENCRYPTED_DATA(0x456e6372),
    DUMMY(0x00000000);

    override fun calculateBinaryLength(charset: Charset, options: PboBinarizationOptions?): Long = 4

    override fun write(buffer: ByteBuffer, charset: Charset, options: PboBinarizationOptions?): Boolean = with(buffer.position()) {
        buffer.putInt(mime)
        buffer.position() == this + 4
    }

    companion object {

        fun fromMime(mime: Int): EntryMimeType? =
            values().firstOrNull { it.mime == mime }
    }


}