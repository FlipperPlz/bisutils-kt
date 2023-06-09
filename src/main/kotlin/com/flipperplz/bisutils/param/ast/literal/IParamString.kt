package com.flipperplz.bisutils.param.ast.literal

import com.flipperplz.bisutils.io.putAsciiZ
import com.flipperplz.bisutils.options.IOptions
import com.flipperplz.bisutils.param.ast.node.IParamLiteral
import com.flipperplz.bisutils.param.options.ParamOptions
import com.flipperplz.bisutils.param.utils.ParamStringType
import java.nio.ByteBuffer

interface IParamString : IParamLiteral, CharSequence {
    val slimStringType: ParamStringType?
    override val paramValue: String?

    override fun isValid(options: IOptions?): Boolean =
        super.isValid(options) && paramValue.let { if(it == null) false else length <= 1024  }

    override fun toParam(): String =
        slimStringType!!.stringify(paramValue ?: "")

    override fun writeValidated(buffer: ByteBuffer, options: ParamOptions?): Boolean {
        buffer.putAsciiZ(paramValue ?: throw Exception(), options?.charset ?: Charsets.UTF_8)
        return true
    }

    override fun read(buffer: ByteBuffer, options: ParamOptions): Boolean =
        throw Exception("Not Supported")

    companion object {
        const val MAX_STRING_LENGTH: Int = 1024
    }
}