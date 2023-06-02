package com.flipperplz.bisutils.binarization.interfaces

import java.nio.ByteBuffer
import java.nio.charset.Charset

interface IBisBinarizable {
    val binaryLength: Long

    fun write(buffer: ByteBuffer, charset: Charset): Boolean
}