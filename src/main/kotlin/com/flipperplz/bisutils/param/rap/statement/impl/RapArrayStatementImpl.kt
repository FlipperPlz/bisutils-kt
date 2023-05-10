package com.flipperplz.bisutils.param.rap.statement.impl

import com.flipperplz.bisutils.param.rap.literal.RapArray
import com.flipperplz.bisutils.param.rap.literal.impl.RapArrayImpl
import com.flipperplz.bisutils.param.rap.node.RapElement
import com.flipperplz.bisutils.param.rap.statement.RapArrayStatement
import com.flipperplz.bisutils.utils.getAsciiZ
import java.nio.ByteBuffer

class RapArrayStatementImpl internal constructor(
    override val parentElement: RapElement?,
    override val slimName: String?
) : RapArrayStatement {
    override lateinit var slimValue: RapArray

    companion object {
        operator fun invoke(parent: RapElement?, buffer: ByteBuffer): RapArrayStatementImpl = RapArrayStatementImpl(parent, buffer.getAsciiZ()).apply {
            slimValue = RapArrayImpl(buffer, parent)
        }
    }

}