package com.flipperplz.bisutils.param.statement.impl

import com.flipperplz.bisutils.param.node.RapElement
import com.flipperplz.bisutils.param.node.RapStatement
import com.flipperplz.bisutils.param.statement.RapClass
import com.flipperplz.bisutils.utils.getAsciiZ
import com.flipperplz.bisutils.utils.getInt
import java.nio.ByteBuffer
import java.nio.ByteOrder

data class RapClassImpl(
    override val slimParent: RapElement?,
    override val slimClassName: String,
    val binaryOffset: Int
) : RapClass {
    override var slimCommands: MutableList<RapStatement> = mutableListOf()
    override lateinit var slimSuperClass: String

    companion object {
        operator fun invoke(parent: RapElement?, buffer: ByteBuffer): RapClassImpl =
            RapClassImpl(parent, buffer.getAsciiZ(), buffer.getInt(ByteOrder.LITTLE_ENDIAN))
    }
}