package com.flipperplz.bisutils.bank.ast.misc

import com.flipperplz.bisutils.bank.ast.IPboFile
import com.flipperplz.bisutils.bank.ast.entry.IPboVersionEntry
import com.flipperplz.bisutils.bank.options.PboOptions
import com.flipperplz.bisutils.binarization.interfaces.IStrictBinaryObject
import com.flipperplz.bisutils.binarization.options.IBinarizationOptions.Companion.DEFAULT_BIS_CHARSET
import com.flipperplz.bisutils.family.IFamilyChild
import com.flipperplz.bisutils.io.putAsciiZ
import com.flipperplz.bisutils.options.IOptions
import java.nio.ByteBuffer

interface IPboProperty : IStrictBinaryObject<PboOptions, PboOptions>, IFamilyChild, Cloneable {
    override val node: IPboFile?
    override val parent: IPboVersionEntry?

    val name: String
    val value: String

    override fun writeValidated(buffer: ByteBuffer, options: PboOptions?): Boolean = with(buffer) {
        putAsciiZ(name, options?.charset ?: DEFAULT_BIS_CHARSET)
        putAsciiZ(value, options?.charset ?: DEFAULT_BIS_CHARSET)
        true
    }

    override fun read(buffer: ByteBuffer, options: PboOptions): Boolean =
        throw Exception("Not Supported!")

    //TODO: CALCULATE LENGTH USING CHARSET IN OPTIONS `options?.charset ?: DEFAULT_BIS_CHARSET`
    override fun calculateBinaryLength(options: PboOptions?): Long = name.length + value.length + 2L

    override fun isValid(options: IOptions?): Boolean =
        !(name.contains('\u0000') || name.isEmpty())

    public override fun clone(): IPboProperty =  super.clone() as IPboProperty
}