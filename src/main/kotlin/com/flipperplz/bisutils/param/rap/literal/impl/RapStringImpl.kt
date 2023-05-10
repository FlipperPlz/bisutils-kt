package com.flipperplz.bisutils.param.rap.literal.impl

import com.flipperplz.bisutils.param.rap.node.RapElement
import com.flipperplz.bisutils.param.rap.literal.RapString
import com.flipperplz.bisutils.utils.getAsciiZ
import java.nio.ByteBuffer

class RapStringImpl(
    override val parentElement: RapElement?,
    override val slimValue: String?
): RapString {
    companion object {
        operator fun invoke(buffer: ByteBuffer, parent: RapElement?): RapStringImpl =
            RapStringImpl(parent, buffer.getAsciiZ())

    }
}