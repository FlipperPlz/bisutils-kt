package com.flipperplz.bisutils.param.rap.statement.impl

import com.flipperplz.bisutils.param.rap.literal.RapArray
import com.flipperplz.bisutils.param.rap.literal.impl.RapArrayImpl
import com.flipperplz.bisutils.param.rap.node.RapElement
import com.flipperplz.bisutils.param.rap.statement.RapFlaggedArrayStatement
import com.flipperplz.bisutils.param.slim.util.ParamOperatorTypes
import com.flipperplz.bisutils.utils.getAsciiZ
import java.nio.ByteBuffer

class RapFlaggedArrayStatementImpl(
    override val parentElement: RapElement?,
    override val slimOperator: ParamOperatorTypes,
    override val slimName: String?
) : RapFlaggedArrayStatement {
    override lateinit var slimValue: RapArray

    companion object {
        operator fun invoke(parent: RapElement?, buffer: ByteBuffer): RapFlaggedArrayStatementImpl = RapFlaggedArrayStatementImpl(parent, ParamOperatorTypes.forFlag(buffer.getInt()) ?: throw Exception(), buffer.getAsciiZ()).apply {
            slimValue =  RapArrayImpl(buffer, this)
        }
    }
}