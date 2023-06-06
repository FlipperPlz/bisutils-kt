package com.flipperplz.bisutils.binarization

import com.flipperplz.bisutils.binarization.interfaces.IStrictBinaryObject
import com.flipperplz.bisutils.binarization.options.BinarizationOptions
import com.flipperplz.bisutils.binarization.options.DebinarizationOptions
import java.nio.ByteBuffer
import java.nio.charset.Charset


abstract class StrictBinaryObject<in BO : BinarizationOptions, in DO : DebinarizationOptions> protected constructor() : BisBinaryObject<BO, DO>(), IStrictBinaryObject<BO, DO> {
    final override fun write(buffer: ByteBuffer, options: BO?): Boolean = super.write(buffer, options)
}