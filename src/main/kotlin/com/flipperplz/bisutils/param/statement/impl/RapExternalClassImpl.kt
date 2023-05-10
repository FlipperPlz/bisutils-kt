package com.flipperplz.bisutils.param.statement.impl

import com.flipperplz.bisutils.param.node.RapElement
import com.flipperplz.bisutils.param.statement.RapExternalClass
import com.flipperplz.bisutils.utils.getAsciiZ
import java.nio.ByteBuffer

data class RapExternalClassImpl(
    override val parent: RapElement?,
    override val slimClassName: String?
) : RapExternalClass {
    companion object {
        operator fun invoke(parent: RapElement?, buffer: ByteBuffer): RapExternalClassImpl =
            RapExternalClassImpl(parent, buffer.getAsciiZ())
    }
}