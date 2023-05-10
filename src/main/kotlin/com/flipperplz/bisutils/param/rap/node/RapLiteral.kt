package com.flipperplz.bisutils.param.rap.node

import com.flipperplz.bisutils.param.rap.literal.impl.*
import com.flipperplz.bisutils.param.slim.node.ParamSlimLiteralBase
import java.nio.ByteBuffer

interface RapLiteral : RapElement, ParamSlimLiteralBase {
    val literalId: Byte

    companion object {
        operator fun invoke(buffer: ByteBuffer, parent: RapElement?): RapLiteral? = when(buffer.get()) {
            0.toByte() -> RapStringImpl(buffer, parent)
            1.toByte() -> RapFloatImpl(buffer, parent)
            2.toByte() -> RapIntImpl(buffer, parent)
            3.toByte() -> RapArrayImpl(buffer, parent)
            else -> null
        }
    }
}