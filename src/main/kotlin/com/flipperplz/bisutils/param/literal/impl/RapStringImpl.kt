package com.flipperplz.bisutils.param.literal.impl

import com.flipperplz.bisutils.param.literal.RapString
import com.flipperplz.bisutils.param.node.RapElement
import com.flipperplz.bisutils.utils.getAsciiZ
import java.nio.ByteBuffer

data class RapStringImpl(
    override val slimParent: RapElement?,
    override val slimValue: String?
) : RapString {
    companion object {
        operator fun invoke(buffer: ByteBuffer, parent: RapElement?): RapStringImpl =
            RapStringImpl(parent, buffer.getAsciiZ())

    }
}