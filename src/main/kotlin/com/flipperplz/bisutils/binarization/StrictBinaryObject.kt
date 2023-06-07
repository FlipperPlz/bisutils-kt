package com.flipperplz.bisutils.binarization

import com.flipperplz.bisutils.binarization.interfaces.IStrictBinaryObject
import com.flipperplz.bisutils.binarization.options.IBinarizationOptions
import java.nio.ByteBuffer

abstract class StrictBinaryObject<in BO : IBinarizationOptions, in DO : IBinarizationOptions> protected constructor() : BisBinaryObject<BO, DO>(), IStrictBinaryObject<BO, DO> {
    final override fun write(buffer: ByteBuffer, options: BO?): Boolean = super.write(buffer, options)
}