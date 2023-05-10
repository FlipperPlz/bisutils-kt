package com.flipperplz.bisutils.param.node

import com.flipperplz.bisutils.param.statement.impl.*
import com.flipperplz.bisutils.param.utils.ParamCommandTypes
import com.flipperplz.bisutils.param.utils.ParamElementTypes
import java.nio.ByteBuffer

interface RapStatement : RapElement {
    val statementId: Byte
    val slimCommandType: ParamCommandTypes

    override val slimType: ParamElementTypes
        get() = slimCommandType.type
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