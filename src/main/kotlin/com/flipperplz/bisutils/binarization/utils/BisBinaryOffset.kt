package com.flipperplz.bisutils.binarization.utils

import com.flipperplz.bisutils.binarization.options.IBinarizationOptions
import com.flipperplz.bisutils.io.putLong
import java.nio.ByteBuffer
import java.nio.ByteOrder

data class BisBinaryOffset(
    var position: Long?,
    var value: Long?
)


fun Collection<BisBinaryOffset>.writeOffsets(buffer: ByteBuffer, options: IBinarizationOptions): Boolean {
    val start = buffer.position()
    forEach {
        buffer.position(it.position!!.toInt())
        buffer.putLong(it.value!!, options.endianness ?: ByteOrder.LITTLE_ENDIAN)
    }
    buffer.position(start)
    return true
}
