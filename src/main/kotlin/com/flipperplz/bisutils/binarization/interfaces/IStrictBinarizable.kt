package com.flipperplz.bisutils.binarization.interfaces

import com.flipperplz.bisutils.binarization.options.IBinarizationOptions
import com.flipperplz.bisutils.utils.IValidatable
import java.nio.ByteBuffer

interface IStrictBinarizable<in B : IBinarizationOptions> : IBinarizable<B>, IValidatable {

    fun writeValidated(buffer: ByteBuffer, options: B?): Boolean

    override fun write(buffer: ByteBuffer, options: B?): Boolean {
        if(options?.skipValidation == true && !isValid(options)) return false
        return writeValidated(buffer, options)
    }
}