package com.flipperplz.bisutils.bank.ast

import com.flipperplz.bisutils.bank.options.PboEntryBinarizationOptions
import com.flipperplz.bisutils.bank.options.PboEntryDebinarizationOptions
import com.flipperplz.bisutils.binarization.interfaces.IStrictBinaryObject
import com.flipperplz.bisutils.family.IFamilyChild
import java.nio.ByteBuffer

interface IPboVFSEntry : IStrictBinaryObject<PboEntryBinarizationOptions, PboEntryDebinarizationOptions>, IFamilyChild, Cloneable {
    val entryName: String
    override val node: IPboFile?
    override val parent: IPboDirectory?

    val path: String
        get() = if(parent == null) entryName else "${parent?.path}\\${entryName}"

    val absolutePath: String
        get() = if(parent == null) entryName else "${parent?.absolutePath}\\${entryName}"

    override fun read(buffer: ByteBuffer, options: PboEntryDebinarizationOptions): Boolean =
        throw Exception("Not Supported!")

    public override fun clone(): IPboVFSEntry = super<Cloneable>.clone() as IPboVFSEntry
}