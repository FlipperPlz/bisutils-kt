package com.flipperplz.bisutils.bank.astImpl.mutable

import com.flipperplz.bisutils.bank.ast.IPboDirectory
import com.flipperplz.bisutils.bank.ast.IPboFile
import com.flipperplz.bisutils.bank.ast.mutable.IMutablePboVFSEntry
import com.flipperplz.bisutils.bank.astImpl.PboVFSEntry

abstract class MutablePboVFSEntry(
        override var familyNode: IPboFile?,
        override var familyParent: IPboDirectory?,
        override var entryName: String
) : PboVFSEntry(familyNode, familyParent, entryName), IMutablePboVFSEntry