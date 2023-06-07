package com.flipperplz.bisutils.bank.ast.mutable

import com.flipperplz.bisutils.bank.ast.IPboVFSEntry

interface IMutablePboVFSEntry : IPboVFSEntry, Cloneable {
    override var entryName: String
    override var node: IMutablePboFile?
    override var parent: IMutablePboDirectory?

    override fun clone(): IMutablePboVFSEntry = super<Cloneable>.clone() as IMutablePboVFSEntry
}