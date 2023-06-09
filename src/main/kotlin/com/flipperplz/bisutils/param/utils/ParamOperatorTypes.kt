package com.flipperplz.bisutils.param.utils

import com.flipperplz.bisutils.binarization.interfaces.IBinarizable
import com.flipperplz.bisutils.io.putInt
import com.flipperplz.bisutils.param.options.ParamOptions
import java.nio.ByteBuffer
import java.nio.ByteOrder

enum class ParamOperatorTypes(val text: String, val flag: Int) : IBinarizable<ParamOptions> {
    ASSIGN("=", 0),
    ADD_ASSIGN("+=", 1),
    SUB_ASSIGN("-=", 2);

    override fun write(buffer: ByteBuffer, options: ParamOptions?): Boolean {
        buffer.putInt(flag, options?.endianness ?: ByteOrder.LITTLE_ENDIAN)
        return true
    }

    override fun calculateBinaryLength(options: ParamOptions?): Long = 1

    companion object {
        fun forFlag(flag: Byte): ParamOperatorTypes? = with(flag.toInt()) { values().firstOrNull { it.flag == this } }
    }
}