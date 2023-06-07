package com.flipperplz.bisutils.binarization.options

import com.flipperplz.bisutils.options.IOptions
import java.nio.ByteOrder
import java.nio.charset.Charset

interface IBinarizationOptions : IOptions {
    companion object {
        val DEFAULT_BIS_CHARSET = Charsets.UTF_8
        val DEFAULT_BIS_ENDIANNESS: ByteOrder = ByteOrder.LITTLE_ENDIAN
    }

    val charset: Charset
    val endianness: ByteOrder
    val skipValidation: Boolean
}