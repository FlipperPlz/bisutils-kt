package com.flipperplz.bisutils.bank.ast.mutable

import com.flipperplz.bisutils.bank.ast.IPboVFSEntry

interface IMutablePboVFSEntry : IPboVFSEntry {
    override var entryName: String
    override var node: IMutablePboFile?
    override var parent: IMutablePboDirectory?
}