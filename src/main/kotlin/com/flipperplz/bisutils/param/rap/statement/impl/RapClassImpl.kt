package com.flipperplz.bisutils.param.rap.statement.impl

import com.flipperplz.bisutils.param.rap.node.RapElement
import com.flipperplz.bisutils.param.rap.node.RapStatement
import com.flipperplz.bisutils.param.rap.statement.RapClass
import com.flipperplz.bisutils.utils.getAsciiZ
import com.flipperplz.bisutils.utils.getInt
import java.nio.ByteBuffer
import java.nio.ByteOrder

class RapClassImpl internal constructor(
    override val parentElement: RapElement?,
    override val slimClassName: String,
    override val binaryOffset: Int
) : RapClass {
    override var slimCommands: MutableList<RapStatement> = mutableListOf()
    override lateinit var slimSuperClass: String

    companion object {
        operator fun invoke(parent: RapElement?, buffer: ByteBuffer): RapClassImpl =
            RapClassImpl(parent, buffer.getAsciiZ(), buffer.getInt(ByteOrder.LITTLE_ENDIAN))
    }
}