package com.flipperplz.bisutils.param.rap.statement.impl

import com.flipperplz.bisutils.param.rap.node.RapElement
import com.flipperplz.bisutils.param.rap.statement.RapExternalClass
import com.flipperplz.bisutils.utils.getAsciiZ
import com.flipperplz.bisutils.utils.getInt
import java.nio.ByteBuffer
import java.nio.ByteOrder

class RapExternalClassImpl(
    override val parentElement: RapElement?,
    override val slimClassName: String?
) : RapExternalClass {
    companion object {
        operator fun invoke(parent: RapElement?, buffer: ByteBuffer): RapExternalClassImpl =
            RapExternalClassImpl(parent, buffer.getAsciiZ())
    }
}