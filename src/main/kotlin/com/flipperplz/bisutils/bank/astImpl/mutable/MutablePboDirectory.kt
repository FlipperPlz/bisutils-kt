package com.flipperplz.bisutils.bank.astImpl.mutable

import com.flipperplz.bisutils.bank.ast.IPboDirectory
import com.flipperplz.bisutils.bank.ast.IPboFile
import com.flipperplz.bisutils.bank.ast.IPboVFSEntry
import com.flipperplz.bisutils.bank.astImpl.PboDirectory

class MutablePboDirectory(
    node: IPboFile?,
    parent: IPboDirectory?,
    entryName: String,
    children: List<IPboVFSEntry>?
) : PboDirectory(node, parent, entryName, children)