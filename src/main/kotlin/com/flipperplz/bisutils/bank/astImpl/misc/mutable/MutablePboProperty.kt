package com.flipperplz.bisutils.bank.astImpl.misc.mutable

import com.flipperplz.bisutils.bank.ast.misc.mutable.IMutablePboProperty
import com.flipperplz.bisutils.bank.astImpl.misc.PboProperty
import com.flipperplz.bisutils.bank.options.PboDebinarizationOptions
import com.flipperplz.bisutils.family.IFamilyNode
import com.flipperplz.bisutils.io.getAsciiZ
import java.nio.ByteBuffer
import java.nio.charset.Charset

class MutablePboProperty(
    override var parent: IFamilyNode?,
    override var node: IFamilyNode?,
    override var name: String,
    override var value: String
) : PboProperty(parent, node, name, value), IMutablePboProperty {
    override fun read(buffer: ByteBuffer, charset: Charset, options: PboDebinarizationOptions): Boolean =when(val parsedName = buffer.getAsciiZ(charset)) {
        "" -> false
        else -> {
            name = parsedName
            value = buffer.getAsciiZ(charset)
            true
        }
    }
}