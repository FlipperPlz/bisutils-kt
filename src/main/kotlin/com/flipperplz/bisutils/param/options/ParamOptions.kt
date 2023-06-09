package com.flipperplz.bisutils.param.options

import com.flipperplz.bisutils.binarization.options.IBinarizationOptions
import java.nio.ByteOrder
import java.nio.charset.Charset

const val DEFAULT_PARAM_SIGNATURE = 0x00726150
class ParamOptions(
    override var charset: Charset = Charsets.UTF_8,
    override var endianness: ByteOrder = ByteOrder.LITTLE_ENDIAN,
    override var skipValidation: Boolean = false,
    var fileSignature: Int = DEFAULT_PARAM_SIGNATURE,
    var pClass: Int? = null,
    var oClass: Int? = null,
    var pArray: Int? = null,
    var oArray: Int? = null,
    var pEnum: Int? = null,
    var oEnum: Int? = null,

) : IBinarizationOptions