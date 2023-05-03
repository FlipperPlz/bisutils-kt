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
    val result = ByteBuffer.allocate(expectedSize)
    if (expectedSize <= 0) return result

    val windowSize = 4096
    val lookaheadBufferSize = 18
    val threshold = 2
    val textBuffer = IntArray(windowSize + lookaheadBufferSize - 1).apply { fill(0x20, 0, windowSize - lookaheadBufferSize) }
    val startPos = position()
    var bytesLeft = expectedSize
    var resultIndex = 0
    var i: Int
    var j: Int
    var bufferIndex: Int = windowSize - lookaheadBufferSize
    var currentByte: Int
    var checksum = 0
    var flags: Int = 0
    fun incrementChecksum(character: Int) = if (useSignedChecksum) checksum += character.toByte() else checksum += character
    while (bytesLeft > 0) {
        flags = flags shr 1
        if (flags and 0x100 == 0) {
            currentByte = get().toInt() and 0xff
            flags = currentByte or 0xff00
        }
        if (flags and 1 != 0) {
            currentByte = get().toInt() and 0xff
            incrementChecksum(currentByte)

            // save byte
            result.put(resultIndex, currentByte.toByte())
            resultIndex++
            bytesLeft--
            // continue decompression
            textBuffer[bufferIndex] = currentByte
            bufferIndex++
            bufferIndex = bufferIndex and (windowSize - 1)
        } else {
            i = get().toInt() and 0xff
            j = (get().toInt() and 0xff) and 0xf0

            i = i or (j shl 4)
            j += threshold

            val ii = bufferIndex - i
            val jj = j + ii
            if (j + 1 > bytesLeft) throw IllegalArgumentException("LZSS overflow")

            for (index in ii..jj) {
                currentByte = textBuffer[index and (windowSize - 1)].toInt()
                incrementChecksum(currentByte)

                // save byte
                result.put(resultIndex, currentByte.toByte())
                resultIndex++
                bytesLeft--
                // continue decompression
                textBuffer[bufferIndex] = currentByte
                bufferIndex++
                bufferIndex = bufferIndex and (windowSize - 1)
            }
        }
    }
    if(
        get() == (checksum shr 0x18).toByte() &&
        get() == ((checksum shr 0x10) and 0xff).toByte() &&
        get() == ((checksum shr 0x8) and 0xff).toByte() &&
        get() == (checksum and  0xff).toByte()
    ) return result

    throw IllegalArgumentException("Checksum mismatch")
}