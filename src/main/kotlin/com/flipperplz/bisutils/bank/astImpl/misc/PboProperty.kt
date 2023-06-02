package com.flipperplz.bisutils.bank.astImpl.misc

import com.flipperplz.bisutils.bank.ast.misc.IPboProperty
import com.flipperplz.bisutils.family.interfaces.IFamilyNode
import com.flipperplz.bisutils.binarization.BisStrictDebinarizable
import java.nio.ByteBuffer
import java.nio.charset.Charset

open class PboProperty(
    override val parent: IFamilyNode?,
    override val node: IFamilyNode?,
    override val name: String,
    override val value: String,
) : BisStrictDebinarizable(), IPboProperty {
    override fun read(buffer: ByteBuffer, charset: Charset): Boolean = throw Exception("Not Supported")
}