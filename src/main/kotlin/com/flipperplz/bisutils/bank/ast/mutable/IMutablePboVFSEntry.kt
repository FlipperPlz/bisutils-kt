package com.flipperplz.bisutils.bank.ast.mutable

import com.flipperplz.bisutils.bank.ast.IPboVFSEntry
import com.flipperplz.bisutils.bank.ast.entry.mutable.IMutablePboDataEntry

interface IMutablePboVFSEntry : IPboVFSEntry, Cloneable {
    override var entryName: String
    override var node: IMutablePboFile?
    override var parent: IMutablePboDirectory?

    override fun clone(): IMutablePboVFSEntry = super<Cloneable>.clone() as IMutablePboVFSEntry
}