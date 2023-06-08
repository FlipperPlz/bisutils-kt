package com.flipperplz.bisutils.io

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.charset.Charset
import kotlin.math.floor


fun ByteBuffer.getBytes(count: Int): ByteArray {
    val bytes = ByteArray(count) // 4 bytes for an integer
    get(bytes)

    return bytes
}

fun ByteBuffer.putLong(value: Long, order: ByteOrder): Unit = this.order().let {
    this.order(order)

    this.put((value shr 56).toByte())
    this.put((value shr 48).toByte())
    this.put((value shr 40).toByte())
    this.put((value shr 32).toByte())
    this.put((value shr 24).toByte())
    this.put((value shr 16).toByte())
    this.put((value shr 8).toByte())
    this.put(value.toByte())

    this.order(it)
}

fun ByteBuffer.getLong(order: ByteOrder): Long = this.order().let {
    this.order(order)

    val value = (this.get().toLong() and 0xFF) shl 56 or
            (this.get().toLong() and 0xFF) shl 48 or
            (this.get().toLong() and 0xFF) shl 40 or
            (this.get().toLong() and 0xFF) shl 32 or
            (this.get().toLong() and 0xFF) shl 24 or
            (this.get().toLong() and 0xFF) shl 16 or
            (this.get().toLong() and 0xFF) shl 8 or
            (this.get().toLong() and 0xFF)
    this.order(it)

    value
}
fun <T> ByteBuffer.peek(peekFun: ByteBuffer.() -> T): T = with(position()) {
    peekFun(this@peek).also { position(this) }
}

fun ByteBuffer.getInt(order: ByteOrder = ByteOrder.LITTLE_ENDIAN): Int =
    ByteBuffer.wrap(getBytes(4)).order(order).getInt(0)

fun ByteBuffer.getFloat(order: ByteOrder = ByteOrder.LITTLE_ENDIAN): Float =
    ByteBuffer.wrap(getBytes(4)).order(order).getFloat(0)

fun ByteBuffer.getAsciiZ(charset: Charset = Charsets.UTF_8, timeout: Int? = null): String {
    val builder = mutableListOf<Byte>()
    while (this.hasRemaining()) {
        if(timeout != null && builder.count() >= timeout) break
        val c = this.get()
        if (c == 0.toByte()) break
        builder.add(c)
    }
    return charset.decode(ByteBuffer.wrap(builder.toByteArray())).toString()
}

fun ByteBuffer.putCompactInt(value: Int) {
    var data = value
    do {
        var current = data % 0x80
        data = floor((data / 0x80000000).toDouble()).toInt()
        if(data != 0) current = current or 0x80
        put(current.toByte())
    } while (data > 0x7F)
    if(data != 0) put(data.toByte())
}

fun ByteBuffer.putAsciiZ(string: String, charset: Charset = Charsets.UTF_8) = put(string.toByteArray(charset) + 0x0)

fun ByteBuffer.getCompactInt(): Int {
    var value: Int = 0
    var i: Int = 0
    while (true) {
        val v: Int = get().toInt()
        value = value or ((v and 0x7F) shl (7 * i))
        if ((v and 0x80) == 0) break
        ++i
    }
    return value
}


fun ByteBuffer.decompress(expectedSize: Long, useSignedChecksum: Boolean): ByteBuffer {
    val result = ByteBuffer.allocate(expectedSize.toInt())
    if (expectedSize <= 0) return result

    val windowSize = 4096
    val lookaheadBufferSize = 18
    val threshold = 2
    val textBuffer =
        IntArray(windowSize + lookaheadBufferSize - 1).apply { fill(0x20, 0, windowSize - lookaheadBufferSize) }
    val startPos = position()
    var bytesLeft = expectedSize
    var resultIndex = 0
    var i: Int
    var j: Int
    var bufferIndex: Int = windowSize - lookaheadBufferSize
    var currentByte: Int
    var checksum = 0
    var flags: Int = 0
    fun incrementChecksum(character: Int) =
        if (useSignedChecksum) checksum += character.toByte() else checksum += character
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
    if (
        get() == (checksum shr 0x18).toByte() &&
        get() == ((checksum shr 0x10) and 0xff).toByte() &&
        get() == ((checksum shr 0x8) and 0xff).toByte() &&
        get() == (checksum and 0xff).toByte()
    ) return result

    throw IllegalArgumentException("Checksum mismatch")
}