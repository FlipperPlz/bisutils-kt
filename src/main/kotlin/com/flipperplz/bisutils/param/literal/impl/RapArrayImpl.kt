package com.flipperplz.bisutils.param.literal.impl

import com.flipperplz.bisutils.param.literal.RapArray
import com.flipperplz.bisutils.param.node.RapElement
import com.flipperplz.bisutils.param.node.RapLiteralBase
import com.flipperplz.bisutils.utils.getCompactInt
import java.nio.ByteBuffer

data class RapArrayImpl(
    override val parent: RapElement?,
    override val slimValue: MutableList<RapLiteralBase> = mutableListOf()
) : RapArray {

    companion object {
        operator fun invoke(buffer: ByteBuffer, parent: RapElement?): RapArrayImpl = RapArrayImpl(parent).apply {
            for (i in 0 until buffer.getCompactInt()) {
                when(buffer.get()) {
                    0.toByte() -> this.slimValue.add(RapStringImpl(buffer, this))
                    1.toByte() -> this.slimValue.add(RapFloatImpl(buffer, this))
                    2.toByte() -> this.slimValue.add(RapIntImpl(buffer, this))
                    3.toByte() -> this.slimValue.add(RapArrayImpl(buffer, this))
                    else -> throw Exception()
                }
            }
        }
    }


}