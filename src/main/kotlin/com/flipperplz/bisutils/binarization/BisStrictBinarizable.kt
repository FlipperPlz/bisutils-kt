package com.flipperplz.bisutils.binarization

import com.flipperplz.bisutils.utils.BisValidatable
import java.nio.ByteBuffer
import java.nio.charset.Charset

interface BisStrictBinarizable : BisBinarizable, BisValidatable {

    fun writeValidated(buffer: ByteBuffer, charset: Charset): Boolean

    override fun write(buffer: ByteBuffer, charset: Charset): Boolean {
        if(!isValid()) return false
        return writeValidated(buffer, charset)
    }
}