package com.flipperplz.bisutils.bank.astImpl

import com.flipperplz.bisutils.bank.ast.IPboDirectory
import com.flipperplz.bisutils.bank.ast.IPboFile
import com.flipperplz.bisutils.bank.ast.IPboVFSEntry

open class PboDirectory(
    override val node: IPboFile?,
    override val parent: IPboDirectory?,
    override val entryName: String,
    override val children: List<IPboVFSEntry>,
) : IPboDirectory