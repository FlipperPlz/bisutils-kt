package com.flipperplz.bisutils.param.ast.misc

import com.flipperplz.bisutils.io.putAsciiZ
import com.flipperplz.bisutils.options.IOptions
import com.flipperplz.bisutils.param.ast.node.IParamElement
import com.flipperplz.bisutils.param.ast.node.IParamElement.Companion.REGEX_ALPHANUM
import com.flipperplz.bisutils.param.ast.node.IParamLiteral
import com.flipperplz.bisutils.param.ast.node.IParamLiteralParent
import com.flipperplz.bisutils.param.ast.node.IParamNumerical
import com.flipperplz.bisutils.param.ast.statement.IParamEnum
import com.flipperplz.bisutils.param.options.ParamOptions
import java.nio.ByteBuffer

interface IParamEnumValue : IParamElement, IParamLiteralParent {
    override val familyParent: IParamEnum?
    val paramName: String?
    val paramValue: IParamNumerical?
    override val familyChildren: List<IParamLiteral>?
        get() = paramValue?.let { listOf(it) } ?: emptyList()

    override fun isValid(options: IOptions?): Boolean = !paramName.isNullOrBlank() && REGEX_ALPHANUM.matches(paramName!!)

    override fun writeValidated(buffer: ByteBuffer, options: ParamOptions?): Boolean {
        buffer.putAsciiZ(paramName!!, options?.charset ?: Charsets.UTF_8)
        //TODO: Auto identify value
        return paramValue!!.writeValidated(buffer, options)
    }

    override fun read(buffer: ByteBuffer, options: ParamOptions): Boolean =
        throw Exception("Not Supported")

    override fun toParam(): String = paramName?.let {
        paramValue?.let { return "$paramName=${paramValue!!.toParam()}" }
        return it
    } ?: throw Exception() //TODO: Param Serialization Exception (isValid was false)

}