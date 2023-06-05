package com.flipperplz.bisutils.bank.options

import com.flipperplz.bisutils.binarization.options.BinarizationOptions
import java.nio.ByteOrder
import java.nio.charset.Charset

open class PboBinarizationOptions(
    val charset: Charset = Charsets.UTF_8,
    val endianness: ByteOrder = ByteOrder.LITTLE_ENDIAN,
) : BinarizationOptions()