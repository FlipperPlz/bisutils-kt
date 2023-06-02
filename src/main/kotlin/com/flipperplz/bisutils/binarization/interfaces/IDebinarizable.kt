package com.flipperplz.bisutils.binarization.interfaces

import com.flipperplz.bisutils.binarization.options.DebinarizationOptions
import java.nio.ByteBuffer
import java.nio.charset.Charset

interface IDebinarizable<in T : DebinarizationOptions> {
    fun read(buffer: ByteBuffer, charset: Charset, options: T): Boolean
}