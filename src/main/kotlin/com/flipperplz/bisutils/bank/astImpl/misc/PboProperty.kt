package com.flipperplz.bisutils.bank.astImpl.misc

import com.flipperplz.bisutils.bank.ast.misc.IPboProperty
import com.flipperplz.bisutils.bank.options.PboDebinarizationOptions
import com.flipperplz.bisutils.bank.utils.IPboBinaryObject
import com.flipperplz.bisutils.family.IFamilyNode
import java.nio.ByteBuffer
import java.nio.charset.Charset

open class PboProperty(
    override val parent: IFamilyNode?,
    override val node: IFamilyNode?,
    override val name: String,
    override val value: String,
) : IPboBinaryObject, IPboProperty {
    override fun read(buffer: ByteBuffer, charset: Charset, options: PboDebinarizationOptions): Boolean =
        throw Exception("Not Supported")
}