package com.flipperplz.bisutils.param.rap.statement.impl

import com.flipperplz.bisutils.param.rap.literal.impl.RapFloatImpl
import com.flipperplz.bisutils.param.rap.literal.impl.RapIntImpl
import com.flipperplz.bisutils.param.rap.literal.impl.RapStringImpl
import com.flipperplz.bisutils.param.rap.node.RapElement
import com.flipperplz.bisutils.param.rap.statement.RapVariableStatement
import com.flipperplz.bisutils.param.slim.commands.ParamSlimVariableStatement
import com.flipperplz.bisutils.param.slim.literals.ParamSlimArray
import com.flipperplz.bisutils.param.slim.node.ParamSlim
import com.flipperplz.bisutils.param.slim.node.ParamSlimLiteral
import com.flipperplz.bisutils.param.slim.util.ParamOperatorTypes
import com.flipperplz.bisutils.utils.getAsciiZ
import java.nio.ByteBuffer

data class RapVariableStatementImpl internal constructor(
    override val parentElement: RapElement?,
    override val slimName: String?
) : RapVariableStatement {
    override lateinit var slimValue: ParamSlimLiteral<*>

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