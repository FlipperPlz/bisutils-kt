package com.flipperplz.bisutils.bank.astImpl.mutable

import com.flipperplz.bisutils.bank.ast.mutable.IMutablePboDirectory
import com.flipperplz.bisutils.bank.ast.mutable.IMutablePboFile
import com.flipperplz.bisutils.bank.ast.mutable.IMutablePboVFSEntry
import com.flipperplz.bisutils.bank.astImpl.PboVFSEntry

abstract class MutablePboVFSEntry(
    override var node: IMutablePboFile?,
    override var parent: IMutablePboDirectory?,
    override var entryName: String
) : PboVFSEntry(node, parent, entryName), IMutablePboVFSEntry