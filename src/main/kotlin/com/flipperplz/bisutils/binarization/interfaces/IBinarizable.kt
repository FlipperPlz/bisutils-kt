package com.flipperplz.bisutils.binarization.interfaces

import com.flipperplz.bisutils.binarization.options.BinarizationOptions
import java.nio.ByteBuffer
import java.nio.charset.Charset

interface IBinarizable<in T: BinarizationOptions> {
    fun calculateBinaryLength(charset: Charset, options: T?) : Long
    fun write(buffer: ByteBuffer, charset: Charset, options: T?): Boolean
}