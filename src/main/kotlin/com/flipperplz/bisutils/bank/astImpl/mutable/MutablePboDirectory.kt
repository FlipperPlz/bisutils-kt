package com.flipperplz.bisutils.bank.astImpl.mutable

import com.flipperplz.bisutils.bank.ast.IPboDirectory
import com.flipperplz.bisutils.bank.ast.IPboFile
import com.flipperplz.bisutils.bank.ast.mutable.IMutablePboDirectory
import com.flipperplz.bisutils.bank.ast.mutable.IMutablePboVFSEntry
import com.flipperplz.bisutils.bank.astImpl.PboDirectory

class MutablePboDirectory(
        override var familyNode: IPboFile?,
        override var familyParent: IPboDirectory?,
        override var entryName: String,
        override var familyChildren: MutableList<IMutablePboVFSEntry>
) : PboDirectory(familyNode, familyParent, entryName, familyChildren), IMutablePboDirectory