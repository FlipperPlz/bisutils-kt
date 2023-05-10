package com.flipperplz.bisutils.param.rap.node

import com.flipperplz.bisutils.param.rap.statement.impl.*
import com.flipperplz.bisutils.param.slim.node.ParamSlimCommand
import java.nio.ByteBuffer

interface RapStatement : ParamSlimCommand, RapElement {
    val statementId: Byte

    companion object {
        operator fun invoke(buffer: ByteBuffer, parent: RapElement?): RapStatement? = when(buffer.get()) {
            0.toByte() -> RapClassImpl(parent, buffer)
            1.toByte() -> RapVariableStatementImpl(parent, buffer)
            2.toByte() -> RapArrayStatementImpl(parent, buffer)
            3.toByte() -> RapExternalClassImpl(parent, buffer)
            4.toByte() -> RapDeleteStatementImpl(parent, buffer)
            5.toByte() -> RapFlaggedArrayStatementImpl(parent, buffer)
            else -> null
        }
    }
}