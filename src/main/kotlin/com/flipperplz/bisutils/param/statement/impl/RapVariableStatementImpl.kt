package com.flipperplz.bisutils.param.statement.impl

import com.flipperplz.bisutils.param.literal.RapArray
import com.flipperplz.bisutils.param.literal.impl.RapFloatImpl
import com.flipperplz.bisutils.param.literal.impl.RapIntImpl
import com.flipperplz.bisutils.param.literal.impl.RapStringImpl
import com.flipperplz.bisutils.param.node.RapElement
import com.flipperplz.bisutils.param.node.RapLiteralBase
import com.flipperplz.bisutils.param.statement.RapVariableStatement
import com.flipperplz.bisutils.param.utils.ParamOperatorTypes
import com.flipperplz.bisutils.utils.getAsciiZ
import java.nio.ByteBuffer

data class RapVariableStatementImpl(
    override val parent: RapElement?,
    override val slimName: String?
) : RapVariableStatement {
    override lateinit var slimValue: RapLiteralBase
    override val slimOperator: ParamOperatorTypes = ParamOperatorTypes.ASSIGN

    override val slimCurrentlyValid: Boolean
        get() = super<RapVariableStatement>.slimCurrentlyValid && slimValue !is RapArray

    companion object {
        operator fun invoke(parent: RapElement?, buffer: ByteBuffer): RapVariableStatementImpl {
            val id = buffer.get()
            return RapVariableStatementImpl(parent, buffer.getAsciiZ()).apply {
                slimValue = when(id) {
                    0.toByte() -> RapStringImpl(buffer, this)
                    1.toByte() -> RapFloatImpl(buffer, this)
                    2.toByte() -> RapIntImpl(buffer, this)
                    else -> throw Exception()
                }
            }
        }

    }
}