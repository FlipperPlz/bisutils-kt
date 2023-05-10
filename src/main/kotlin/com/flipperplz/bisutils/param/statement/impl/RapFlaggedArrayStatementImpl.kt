package com.flipperplz.bisutils.param.statement.impl

import com.flipperplz.bisutils.param.literal.RapArray
import com.flipperplz.bisutils.param.literal.impl.RapArrayImpl
import com.flipperplz.bisutils.param.node.RapElement
import com.flipperplz.bisutils.param.statement.RapVariableStatement
import com.flipperplz.bisutils.param.utils.ParamOperatorTypes
import com.flipperplz.bisutils.utils.getAsciiZ
import java.nio.ByteBuffer

class RapFlaggedArrayStatementImpl internal constructor(
    override val parent: RapElement?,
    override val slimOperator: ParamOperatorTypes,
    override val slimName: String?
) : RapVariableStatement {
    override lateinit var slimValue: RapArray

    override val statementId: Byte
        get() = 5

    companion object {
        operator fun invoke(parent: RapElement?, buffer: ByteBuffer): RapFlaggedArrayStatementImpl = RapFlaggedArrayStatementImpl(parent, ParamOperatorTypes.forFlag(buffer.getInt()) ?: throw Exception(), buffer.getAsciiZ()).apply {
            slimValue =  RapArrayImpl(buffer, this)
        }
    }
}