package com.flipperplz.bisutils.binarization.interfaces

import com.flipperplz.bisutils.binarization.options.IBinarizationOptions
import java.nio.ByteBuffer

interface IBinarizable<in T: IBinarizationOptions> {
    fun calculateBinaryLength(options: T?) : Long
    fun write(buffer: ByteBuffer, options: T?): Boolean
}