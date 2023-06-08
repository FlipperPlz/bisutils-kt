package com.flipperplz.bisutils.bank.astImpl.mutable

import com.flipperplz.bisutils.bank.ast.IPboDirectory
import com.flipperplz.bisutils.bank.ast.IPboFile
import com.flipperplz.bisutils.bank.ast.IPboVFSEntry
import com.flipperplz.bisutils.bank.ast.mutable.IMutablePboDirectory
import com.flipperplz.bisutils.bank.ast.mutable.IMutablePboFile
import com.flipperplz.bisutils.bank.ast.mutable.IMutablePboVFSEntry
import com.flipperplz.bisutils.bank.astImpl.PboDirectory

class MutablePboDirectory(
    override var node: IPboFile?,
    override var parent: IPboDirectory?,
    override var entryName: String,
    override var children: MutableList<IMutablePboVFSEntry>
) : PboDirectory(node, parent, entryName, children), IMutablePboDirectory