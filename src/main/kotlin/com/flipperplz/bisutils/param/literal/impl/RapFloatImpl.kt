package com.flipperplz.bisutils.param.literal.impl

import com.flipperplz.bisutils.param.literal.RapFloat
import com.flipperplz.bisutils.param.node.RapElement
import com.flipperplz.bisutils.utils.getFloat
import java.nio.ByteBuffer
import java.nio.ByteOrder

data class RapFloatImpl(
    override val slimParent: RapElement?,
    override val slimValue: Float?
) : RapFloat {
    companion object {
        operator fun invoke(buffer: ByteBuffer, parent: RapElement?): RapFloatImpl =
            RapFloatImpl(parent, buffer.getFloat(ByteOrder.LITTLE_ENDIAN))

    }
}