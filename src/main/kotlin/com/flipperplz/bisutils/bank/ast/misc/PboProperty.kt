package com.flipperplz.bisutils.bank.ast.misc

import com.flipperplz.bisutils.utils.BisFamily
import com.flipperplz.bisutils.utils.BisStrictBinarizable
import com.flipperplz.bisutils.utils.putAsciiZ
import java.nio.ByteBuffer
import java.nio.charset.Charset

interface PboProperty : BisStrictBinarizable, PboElement {
    val name: String
    val value: String

    override val children: List<Any>
        get() = listOf(name, value)

    override fun writeValidated(buffer: ByteBuffer, charset: Charset): Boolean {
        buffer.putAsciiZ(name, charset)
        buffer.putAsciiZ(value, charset)
        return true
    }

    override val binaryLength: Long
        get() = name.length + value.length + 2L

    override fun isValid(): Boolean {
        return true //TODO Value cannot be empty
    }
}