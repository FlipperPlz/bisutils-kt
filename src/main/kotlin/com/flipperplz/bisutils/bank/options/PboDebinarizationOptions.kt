package com.flipperplz.bisutils.bank.options

import com.flipperplz.bisutils.binarization.options.DebinarizationOptions
import java.nio.ByteOrder
import java.nio.charset.Charset

open class PboDebinarizationOptions(
    val charset: Charset = Charsets.UTF_8,
    val endianness: ByteOrder = ByteOrder.LITTLE_ENDIAN,
) : DebinarizationOptions()