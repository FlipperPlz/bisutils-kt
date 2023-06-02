package com.flipperplz.bisutils.binarization

import com.flipperplz.bisutils.binarization.interfaces.IBisBinarizable
import java.nio.ByteBuffer
import java.nio.charset.Charset

abstract class BisDebinarizable protected constructor(): IBisBinarizable {

    protected constructor(buffer: ByteBuffer, charset: Charset) : this()

    abstract fun read(buffer: ByteBuffer, charset: Charset): Boolean
}