package com.flipperplz.bisutils.bank.ast.mutable

import com.flipperplz.bisutils.bank.ast.IPboDirectory
import com.flipperplz.bisutils.bank.ast.IPboFile
import com.flipperplz.bisutils.bank.ast.IPboVFSEntry

interface IMutablePboVFSEntry : IPboVFSEntry, Cloneable {
    override var entryName: String
    override var node: IPboFile?
    override var parent: IPboDirectory?

    override fun clone(): IMutablePboVFSEntry = super<Cloneable>.clone() as IMutablePboVFSEntry
}