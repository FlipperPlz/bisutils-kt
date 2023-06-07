package com.flipperplz.bisutils.binarization.interfaces

import com.flipperplz.bisutils.binarization.options.IBinarizationOptions
import java.nio.ByteBuffer

interface IDebinarizable<in T : IBinarizationOptions> {
    fun read(buffer: ByteBuffer, options: T): Boolean
}