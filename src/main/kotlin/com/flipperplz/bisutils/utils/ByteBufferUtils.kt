package com.flipperplz.bisutils.utils

import java.nio.ByteBuffer
import java.nio.charset.Charset

fun ByteBuffer.getAsciiZ(charset: Charset = Charsets.UTF_8): String {
    val builder = StringBuilder()
    val decoder = charset.newDecoder()
    val terminator = ByteBuffer.allocate(1).put(0).flip()
    while (this.hasRemaining()) {
        val buffer = ByteBuffer.allocate(1)
        while (this.remaining() > 0 && this.compareTo(terminator) != 0) {
            buffer.put(this.get())
        }
        buffer.flip()
        val decoded: String = decoder.decode(buffer).toString()
        if (this.remaining() > 0) {
            this.get() // Consume the null terminator
        }
        if (decoded.isEmpty()) {
            break
        }
        builder.append(decoded)
    }
    return builder.toString()
}

fun ByteBuffer.getCompactInt(): Int {
    var value = 0
    for (i in 0..4) {
        val v = get().toInt() and 0xFF
        value = value or ((v and 0x7F) shl (7 * i))
        if ((v and 0x80) == 0) {
            break
        }
    }
    return value
}