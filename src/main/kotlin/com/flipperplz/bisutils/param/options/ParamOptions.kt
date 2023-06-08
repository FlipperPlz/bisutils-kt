package com.flipperplz.bisutils.param.options

import com.flipperplz.bisutils.binarization.options.IBinarizationOptions
import java.nio.ByteOrder
import java.nio.charset.Charset

class ParamOptions(
    override val charset: Charset,
    override val endianness: ByteOrder
) : IBinarizationOptions {
    override val skipValidation: Boolean = false
}