package com.flipperplz.bisutils.param.ast.literal

import com.flipperplz.bisutils.io.putInt
import com.flipperplz.bisutils.param.ast.node.IParamNumerical
import com.flipperplz.bisutils.param.options.ParamOptions
import java.nio.ByteBuffer
import java.nio.ByteOrder

interface IParamInt : IParamNumerical {
    companion object;
    override val paramValue: Int?

    override fun writeValidated(buffer: ByteBuffer, options: ParamOptions?): Boolean {
        buffer.putInt(paramValue ?: throw Exception(), options?.endianness ?: ByteOrder.LITTLE_ENDIAN )
        return true
    }

    override fun read(buffer: ByteBuffer, options: ParamOptions): Boolean =
        throw Exception("Not Supported")
}