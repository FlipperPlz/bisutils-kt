package com.flipperplz.bisutils.param.ast.statement

import com.flipperplz.bisutils.io.putAsciiZ
import com.flipperplz.bisutils.options.IOptions
import com.flipperplz.bisutils.param.ast.node.IParamElement
import com.flipperplz.bisutils.param.ast.node.IParamStatement
import com.flipperplz.bisutils.param.options.ParamOptions
import java.nio.ByteBuffer

interface IParamExternalClass : IParamStatement {
    companion object;
    val paramClassname: String?

    override fun isValid(options: IOptions?): Boolean =
            paramClassname != null &&
            IParamElement.REGEX_ALPHANUM.matches(paramClassname!!)

    override fun writeValidated(buffer: ByteBuffer, options: ParamOptions?): Boolean {
        buffer.putAsciiZ(paramClassname!!, options?.charset ?: Charsets.UTF_8)
        return true
    }

    override fun read(buffer: ByteBuffer, options: ParamOptions): Boolean =
            throw Exception("Not Supported")

    override fun toParam(): String = "class $paramClassname;"
}