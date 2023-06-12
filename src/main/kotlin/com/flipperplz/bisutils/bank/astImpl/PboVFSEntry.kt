package com.flipperplz.bisutils.bank.astImpl

import com.flipperplz.bisutils.bank.ast.IPboDirectory
import com.flipperplz.bisutils.bank.ast.IPboFile
import com.flipperplz.bisutils.bank.ast.IPboVFSEntry

abstract class PboVFSEntry(
        override val familyNode: IPboFile?,
        override val familyParent: IPboDirectory?,
        override val entryName: String
) : IPboVFSEntry