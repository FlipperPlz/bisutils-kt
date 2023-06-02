package com.flipperplz.bisutils.bank.ast.misc

import com.flipperplz.bisutils.bank.options.PboBinarizationOptions
import com.flipperplz.bisutils.bank.utils.IPboBinaryObject
import com.flipperplz.bisutils.family.IFamilyChild
import com.flipperplz.bisutils.io.putAsciiZ
import java.nio.ByteBuffer
import java.nio.charset.Charset

interface IPboProperty : IPboBinaryObject, IFamilyChild {
    val name: String
    val value: String

    override fun writeValidated(buffer: ByteBuffer, charset: Charset, options: PboBinarizationOptions?): Boolean = with(buffer) {
        putAsciiZ(name, charset)
        putAsciiZ(value, charset)
        true
    }

    override fun calculateBinaryLength(charset: Charset, options: PboBinarizationOptions?): Long = name.length + value.length + 2L

    override fun isValid(): Boolean = !(name.contains('\u0000') || name.isEmpty())
}