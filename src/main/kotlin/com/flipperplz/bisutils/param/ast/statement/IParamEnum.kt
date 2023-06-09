package com.flipperplz.bisutils.param.ast.statement

import com.flipperplz.bisutils.family.IFamilyParent
import com.flipperplz.bisutils.io.putInt
import com.flipperplz.bisutils.options.IOptions
import com.flipperplz.bisutils.param.ast.misc.IParamEnumValue
import com.flipperplz.bisutils.param.ast.node.IParamStatement
import com.flipperplz.bisutils.param.options.ParamOptions
import java.nio.ByteBuffer
import java.nio.ByteOrder

interface IParamEnum: IParamStatement, IFamilyParent {
    override val children: List<IParamEnumValue>?
    override fun isValid(options: IOptions?): Boolean = children?.all { it.isValid(options) } ?: true

    override fun writeValidated(buffer: ByteBuffer, options: ParamOptions?): Boolean {
        options?.currentOffset = buffer.position()
        buffer.putInt(children!!.count(), options?.endianness ?: ByteOrder.LITTLE_ENDIAN)
        children!!.forEach { if(!it.writeValidated(buffer, options)) return false }
        return true
    }

    override fun read(buffer: ByteBuffer, options: ParamOptions): Boolean =
            throw Exception("Not Supported")

    override fun toParam(): String =
        if(children.isNullOrEmpty()) ""
        else "enum {\n${children?.joinToString(separator = ",\n") { it.toParam() }}\n};"
}