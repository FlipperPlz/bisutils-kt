package com.flipperplz.bisutils.param.ast.literal

import com.flipperplz.bisutils.io.putCompactInt
import com.flipperplz.bisutils.options.IOptions
import com.flipperplz.bisutils.param.ast.node.IParamLiteralParent
import com.flipperplz.bisutils.param.ast.node.IParamLiteral
import com.flipperplz.bisutils.param.options.ParamOptions
import java.nio.ByteBuffer

interface IParamArray : IParamLiteral, IParamLiteralParent {
    companion object;
    override val familyChildren: List<IParamLiteral>? get() = paramValue
    override val familyParent: IParamLiteralParent?
    override val paramValue: List<IParamLiteral>?
    override fun isValid(options: IOptions?): Boolean = super.isValid(options) && paramValue?.all { it.isValid(options) } ?: false

    override fun toParam(): String =
        (paramValue ?: emptyList()).joinToString(", ", prefix = "{", postfix = "}") { it.toParam() }

    override fun writeValidated(buffer: ByteBuffer, options: ParamOptions?): Boolean {
        buffer.putCompactInt(familyChildren!!.count())
        familyChildren!!.forEach { if(!it.writeValidated(buffer, options)) return false }
        return true
    }

    override fun read(buffer: ByteBuffer, options: ParamOptions): Boolean =
        throw Exception("Not Supported")
}