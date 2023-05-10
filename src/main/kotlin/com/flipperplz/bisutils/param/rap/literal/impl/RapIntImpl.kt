package com.flipperplz.bisutils.param.rap.literal.impl

import com.flipperplz.bisutils.param.rap.node.RapElement
import com.flipperplz.bisutils.param.rap.literal.RapInt
import com.flipperplz.bisutils.utils.getInt
import java.nio.ByteBuffer
import java.nio.ByteOrder

data class RapIntImpl internal constructor(
    override val parentElement: RapElement?,
    override val slimValue: Int?
): RapInt {
    companion object {
        operator fun invoke(buffer: ByteBuffer, parent: RapElement?): RapIntImpl =
            RapIntImpl(parent, buffer.getInt(ByteOrder.LITTLE_ENDIAN))

    }
}