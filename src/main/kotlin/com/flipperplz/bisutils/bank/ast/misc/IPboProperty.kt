package com.flipperplz.bisutils.bank.ast.misc

import com.flipperplz.bisutils.family.interfaces.IFamilyChild
import com.flipperplz.bisutils.binarization.interfaces.IStrictBinarizable
import com.flipperplz.bisutils.io.putAsciiZ
import java.nio.ByteBuffer
import java.nio.charset.Charset

interface IPboProperty : IStrictBinarizable, IFamilyChild {
    val name: String
    val value: String

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