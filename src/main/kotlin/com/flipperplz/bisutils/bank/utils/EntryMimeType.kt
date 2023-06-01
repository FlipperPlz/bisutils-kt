package com.flipperplz.bisutils.bank.utils

import com.flipperplz.bisutils.binarization.BisBinarizable
import java.nio.ByteBuffer
import java.nio.charset.Charset

enum class EntryMimeType(
    val mime: Int
) : BisBinarizable {
    VERSION(0x56657273),
    NORMAL_DATA(0x43707273),
    ENCRYPTED_DATA(0x456e6372),
    DUMMY(0x00000000);

    override val binaryLength: Long = 4

    override fun write(buffer: ByteBuffer, charset: Charset): Boolean = with(buffer.position()) {
        buffer.putInt(mime)
        buffer.position() == this + 4
    }

    companion object {

        fun fromMime(mime: Int): EntryMimeType? {
            return values().firstOrNull { it.mime == mime }
        }
    }


}