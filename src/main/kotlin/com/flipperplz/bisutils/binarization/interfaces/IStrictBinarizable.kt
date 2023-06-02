package com.flipperplz.bisutils.binarization.interfaces

import com.flipperplz.bisutils.utils.IValidatable
import java.nio.ByteBuffer
import java.nio.charset.Charset

interface IStrictBinarizable : IBisBinarizable, IValidatable {

    fun writeValidated(buffer: ByteBuffer, charset: Charset): Boolean

    override fun write(buffer: ByteBuffer, charset: Charset): Boolean {
        if(!isValid()) return false
        return writeValidated(buffer, charset)
    }
}