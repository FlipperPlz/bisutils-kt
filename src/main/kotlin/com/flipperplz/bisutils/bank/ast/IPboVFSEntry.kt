package com.flipperplz.bisutils.bank.ast

import com.flipperplz.bisutils.bank.options.PboEntryBinarizationOptions
import com.flipperplz.bisutils.bank.options.PboEntryDebinarizationOptions
import com.flipperplz.bisutils.binarization.interfaces.IStrictBinaryObject
import com.flipperplz.bisutils.family.IFamilyChild

interface IPboVFSEntry : IStrictBinaryObject<PboEntryBinarizationOptions, PboEntryDebinarizationOptions>, IFamilyChild {
    val entryName: String
    override val node: IPboFile?
    override val parent: IPboDirectory?

    val path: String
        get() = if(parent == null) entryName else "${parent?.path}\\${entryName}"

    val absolutePath: String
        get() =  if(parent == null) entryName else "${parent?.absolutePath}\\${entryName}"
}