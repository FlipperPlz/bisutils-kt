package com.flipperplz.bisutils.param.statement.impl

import com.flipperplz.bisutils.param.node.RapElement
import com.flipperplz.bisutils.param.statement.RapDeleteStatement
import com.flipperplz.bisutils.utils.getAsciiZ
import java.nio.ByteBuffer

data class RapDeleteStatementImpl(
    override val parent: RapElement?,
    override val slimDeleteTarget: String?
) : RapDeleteStatement {
    companion object {
        operator fun invoke(parent: RapElement?, buffer: ByteBuffer): RapDeleteStatementImpl =
            RapDeleteStatementImpl(parent, buffer.getAsciiZ())
    }
}