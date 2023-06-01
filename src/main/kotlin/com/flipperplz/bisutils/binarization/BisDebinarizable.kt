package com.flipperplz.bisutils.binarization

import java.nio.ByteBuffer
import java.nio.charset.Charset

abstract class BisDebinarizable protected constructor(): BisBinarizable {

    protected constructor(buffer: ByteBuffer, charset: Charset) : this()

    abstract fun read(buffer: ByteBuffer, charset: Charset)
}