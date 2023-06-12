package com.flipperplz.bisutils.param.ast

import com.flipperplz.bisutils.family.IFamilyNode
import com.flipperplz.bisutils.family.IFamilyParent
import com.flipperplz.bisutils.io.putInt
import com.flipperplz.bisutils.param.ast.node.IParamStatementHolder
import com.flipperplz.bisutils.param.options.DEFAULT_PARAM_SIGNATURE
import com.flipperplz.bisutils.param.options.ParamOptions
import java.nio.ByteBuffer
import java.nio.ByteOrder

interface IParamFile : IFamilyNode, IParamStatementHolder {
    companion object;
    val paramName: String?
    override val familyParent: IFamilyParent? get() = null
    override val familyNode: IParamFile? get() = this

    override fun toParam(): String = super.toParam()

    override fun writeValidated(buffer: ByteBuffer, options: ParamOptions?): Boolean {
        buffer.putInt(options?.fileSignature ?: DEFAULT_PARAM_SIGNATURE, options?.endianness ?: ByteOrder.LITTLE_ENDIAN )

        return true
    }
}