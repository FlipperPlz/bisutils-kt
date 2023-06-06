package com.flipperplz.bisutils.binarization.options

import com.flipperplz.bisutils.options.BisOptions
import java.nio.ByteOrder
import java.nio.charset.Charset

val DEFAULT_BIS_CHARSET = Charsets.UTF_8
val DEFAULT_BIS_ENDIANNESS: ByteOrder = ByteOrder.LITTLE_ENDIAN

open class BinarizationOptions(
    val charset: Charset = DEFAULT_BIS_CHARSET,
    val endianness: ByteOrder = DEFAULT_BIS_ENDIANNESS,
    val skipValidation: Boolean = true
) : BisOptions()