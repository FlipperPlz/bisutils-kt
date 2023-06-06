package com.flipperplz.bisutils.bank.ast.misc

import com.flipperplz.bisutils.bank.ast.IPboFile
import com.flipperplz.bisutils.bank.ast.entry.IPboVersionEntry
import com.flipperplz.bisutils.bank.options.PboBinarizationOptions
import com.flipperplz.bisutils.bank.options.PboEntryBinarizationOptions
import com.flipperplz.bisutils.bank.options.PboEntryDebinarizationOptions
import com.flipperplz.bisutils.bank.utils.IPboBinaryObject
import com.flipperplz.bisutils.binarization.options.DEFAULT_BIS_CHARSET
import com.flipperplz.bisutils.family.IFamilyChild
import com.flipperplz.bisutils.family.IFamilyNode
import com.flipperplz.bisutils.family.IFamilyParent
import com.flipperplz.bisutils.io.putAsciiZ
import com.flipperplz.bisutils.options.BisOptions
import java.nio.ByteBuffer
import java.nio.charset.Charset

interface IPboProperty : IPboBinaryObject, IFamilyChild {
    override val node: IPboFile?
    override val parent: IPboVersionEntry?

    val name: String
    val value: String

    override fun writeValidated(buffer: ByteBuffer, options: PboEntryBinarizationOptions?): Boolean = with(buffer) {
        putAsciiZ(name, options?.charset ?: DEFAULT_BIS_CHARSET)
        putAsciiZ(value, options?.charset ?: DEFAULT_BIS_CHARSET)
        true
    }

    override fun read(buffer: ByteBuffer, options: PboEntryDebinarizationOptions): Boolean =
        throw Exception("Not Supported!")

    //TODO: CALCULATE LENGTH USING CHARSET IN OPTIONS `options?.charset ?: DEFAULT_BIS_CHARSET`
    override fun calculateBinaryLength(options: PboEntryBinarizationOptions?): Long = name.length + value.length + 2L

    override fun isValid(options: BisOptions?): Boolean =
        !(name.contains('\u0000') || name.isEmpty())
}