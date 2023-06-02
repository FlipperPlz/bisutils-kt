package com.flipperplz.bisutils.binarization.interfaces

import com.flipperplz.bisutils.binarization.options.BinarizationOptions
import com.flipperplz.bisutils.utils.IValidatable
import java.nio.ByteBuffer
import java.nio.charset.Charset

interface IStrictBinarizable<in B : BinarizationOptions> : IBinarizable<B>, IValidatable {

    fun writeValidated(buffer: ByteBuffer, charset: Charset, options: B?): Boolean

    override fun write(buffer: ByteBuffer, charset: Charset, options: B?): Boolean {
        if(!isValid()) return false
        return writeValidated(buffer, charset, options)
    }
}