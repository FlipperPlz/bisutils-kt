package com.flipperplz.bisutils.utils

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.charset.Charset


fun ByteBuffer.getBytes(count: Int): ByteArray {
    val bytes = ByteArray(count) // 4 bytes for an integer
    get(bytes)

    return bytes
}

fun ByteBuffer.getInt(order: ByteOrder = ByteOrder.LITTLE_ENDIAN): Int = ByteBuffer.wrap(getBytes(4)).order(order).getInt(0)
fun ByteBuffer.getFloat(order: ByteOrder = ByteOrder.LITTLE_ENDIAN): Float = ByteBuffer.wrap(getBytes(4)).order(order).getFloat(0)

fun ByteBuffer.getAsciiZ(charset: Charset = Charsets.UTF_8): String {
    val builder = mutableListOf<Byte>()
    while (this.hasRemaining()) {
        val c = this.get()
        if(c == 0.toByte()) break
        builder.add(c)
    }
    return charset.decode(ByteBuffer.wrap(builder.toByteArray())).toString()
}

fun ByteBuffer.getCompactInt(): Int {
    var value: Int = 0
    var i: Int = 0
    while (true) {
        val v: Int = get().toInt()
        value = value or ((v and 0x7F) shl (7 * i))
        if ((v and 0x80) == 0) break
        ++i;
    }
    return value
}


fun ByteBuffer.decompress(expectedSize: Int, useSignedChecksum: Boolean): ByteBuffer {
    val N = 4096
    val F = 18
    val THRESHOLD = 2
    val text_buf = CharArray(N + F - 1)
    val dst = ByteBuffer.allocate(expectedSize)

    if (expectedSize <= 0) return dst

    val startPos = position()
    var bytesLeft = expectedSize
    var iDst = 0

    var i: Int
    var j: Int
    var r: Int
    var c: Int
    var csum = 0
    var flags: Int
    for (i in 0 until N - F) text_buf[i] = ' '
    r = N - F
    flags = 0
    while (bytesLeft > 0) {
        if ((flags ushr  1) and 256 == 0) {
            val c = get().toInt() and 0xff
            flags = c or 0xff00
        }
        if (flags and 1 != 0) {
            val c = get().toInt() and 0xff
            if (useSignedChecksum)
                csum += c.toByte()
            else
                csum += c

            // save byte
            dst.put(iDst, c.toByte())
            iDst++
            bytesLeft--
            // continue decompression
            text_buf[r] = c.toChar()
            r++
            r = r and (N - 1)
        } else {
            val i = get().toInt() and 0xff
            val j = get().toInt() and 0xff
            var ii = r - i
            val jj = j + ii
            if (j + 1 > bytesLeft) {
                throw IllegalArgumentException("LZSS overflow")
            }

            for (index in ii..jj) {
                c = text_buf[index and (N - 1)].toInt()
                if (useSignedChecksum)
                    csum += c.toByte()
                else
                    csum += c

                // save byte
                dst.put(iDst, c.toByte())
                iDst++
                bytesLeft--
                // continue decompression
                text_buf[r] = c.toChar()
                r++
                r = r and (N - 1)
            }
        }
    }

    val csData = ByteArray(4)
    get(csData, 0, 4)
    val csr = ByteBuffer.wrap(csData).getInt()
    if (csr != csum) throw IllegalArgumentException("Checksum mismatch")

    return dst
}