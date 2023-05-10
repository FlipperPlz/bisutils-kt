package com.flipperplz.bisutils.param.rap.literal.impl

import com.flipperplz.bisutils.param.rap.node.RapElement
import com.flipperplz.bisutils.param.rap.literal.RapFloat
import com.flipperplz.bisutils.utils.getFloat
import java.nio.ByteBuffer
import java.nio.ByteOrder

class RapFloatImpl(
    override val parentElement: RapElement?,
    override val slimValue: Float?
): RapFloat {
    companion object {
        operator fun invoke(buffer: ByteBuffer, parent: RapElement?): RapFloatImpl =
            RapFloatImpl(parent, buffer.getFloat(ByteOrder.LITTLE_ENDIAN),)

    }
}