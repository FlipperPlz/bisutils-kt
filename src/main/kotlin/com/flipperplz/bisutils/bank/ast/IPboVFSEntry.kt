package com.flipperplz.bisutils.bank.ast

import com.flipperplz.bisutils.bank.options.PboOptions
import com.flipperplz.bisutils.binarization.interfaces.IStrictBinaryObject
import com.flipperplz.bisutils.family.IFamilyChild
import java.nio.ByteBuffer

interface IPboVFSEntry : IStrictBinaryObject<PboOptions, PboOptions>, IFamilyChild, Cloneable {
    val entryName: String
    override val familyNode: IPboFile?
    override val familyParent: IPboDirectory?

    val path: String
        get() = if(familyParent == null) entryName else "${familyParent?.path}\\${entryName}"

    val absolutePath: String
        get() = if(familyParent == null) entryName else "${familyParent?.absolutePath}\\${entryName}"

    override fun read(buffer: ByteBuffer, options: PboOptions): Boolean =
        throw Exception("Not Supported!")

    public override fun clone(): IPboVFSEntry = super<Cloneable>.clone() as IPboVFSEntry
}