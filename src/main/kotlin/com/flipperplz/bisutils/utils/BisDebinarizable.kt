package com.flipperplz.bisutils.utils

import java.nio.ByteBuffer
import java.nio.charset.Charset

abstract class BisDebinarizable protected constructor(): BisBinarizable {

    constructor(buffer: ByteBuffer, charset: Charset) : this()

    abstract fun read(buffer: ByteBuffer, charset: Charset)
}