package com.flipperplz.bisutils.binarization

import com.flipperplz.bisutils.binarization.interfaces.IStrictBinarizable
import java.nio.ByteBuffer
import java.nio.charset.Charset

abstract class BisStrictDebinarizable protected constructor() : BisDebinarizable(), IStrictBinarizable {
    protected constructor(buffer: ByteBuffer, charset: Charset) : this()
}