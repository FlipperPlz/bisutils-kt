package com.flipperplz.bisutils.bank.options

import com.flipperplz.bisutils.binarization.options.BinarizationOptions
import java.nio.ByteOrder
import java.nio.charset.Charset

open class PboBinarizationOptions(
    charset: Charset = Charsets.UTF_8,
    endianness: ByteOrder = ByteOrder.LITTLE_ENDIAN,
    skipValidation: Boolean = false
) : BinarizationOptions(charset, endianness, skipValidation)