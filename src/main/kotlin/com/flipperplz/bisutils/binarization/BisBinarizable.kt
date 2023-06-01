package com.flipperplz.bisutils.binarization

import java.nio.ByteBuffer
import java.nio.charset.Charset

interface BisBinarizable {
    val binaryLength: Long

    fun write(buffer: ByteBuffer, charset: Charset): Boolean
}