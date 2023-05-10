package com.flipperplz.bisutils.param.statement.impl

import com.flipperplz.bisutils.param.literal.RapArray
import com.flipperplz.bisutils.param.literal.impl.RapArrayImpl
import com.flipperplz.bisutils.param.node.RapElement
import com.flipperplz.bisutils.param.statement.RapVariableStatement
import com.flipperplz.bisutils.param.utils.ParamOperatorTypes
import com.flipperplz.bisutils.utils.getAsciiZ
import java.nio.ByteBuffer

class RapArrayStatementImpl internal constructor(
    override val parent: RapElement?,
    override val slimName: String?
) : RapVariableStatement {
    override lateinit var slimValue: RapArray
    override val slimOperator: ParamOperatorTypes = ParamOperatorTypes.ASSIGN

    override val statementId: Byte
        get() = 2

    companion object {
        operator fun invoke(parent: RapElement?, buffer: ByteBuffer): RapArrayStatementImpl = RapArrayStatementImpl(parent, buffer.getAsciiZ()).apply {
            slimValue = RapArrayImpl(buffer, parent)
        }
    }

    override val slimCurrentlyValid: Boolean
        get() = super<RapVariableStatement>.slimCurrentlyValid

}