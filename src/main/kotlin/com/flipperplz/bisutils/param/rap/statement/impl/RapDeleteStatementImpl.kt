package com.flipperplz.bisutils.param.rap.statement.impl

import com.flipperplz.bisutils.param.rap.node.RapElement
import com.flipperplz.bisutils.param.rap.statement.RapDeleteStatement
import com.flipperplz.bisutils.param.slim.commands.ParamSlimDeleteStatement
import com.flipperplz.bisutils.utils.getAsciiZ
import java.nio.ByteBuffer

class RapDeleteStatementImpl internal constructor(
    override val parentElement: RapElement?,
    override val slimDeleteTarget: String?
) : RapDeleteStatement {
    companion object {
        operator fun invoke(parent: RapElement?, buffer: ByteBuffer): RapDeleteStatementImpl =
            RapDeleteStatementImpl(parent, buffer.getAsciiZ())
    }
}