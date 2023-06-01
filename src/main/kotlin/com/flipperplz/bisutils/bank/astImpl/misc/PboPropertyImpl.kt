package com.flipperplz.bisutils.bank.astImpl.misc

import com.flipperplz.bisutils.bank.ast.misc.PboProperty
import com.flipperplz.bisutils.family.interfaces.FamilyNode
import com.flipperplz.bisutils.binarization.BisStrictDebinarizable
import java.nio.ByteBuffer
import java.nio.charset.Charset

open class PboPropertyImpl(
    override val parent: FamilyNode?,
    override val node: FamilyNode?,
    override val name: String,
    override val value: String,
) : BisStrictDebinarizable(), PboProperty {
    override fun read(buffer: ByteBuffer, charset: Charset) {
        TODO("Not yet implemented")
    }
}