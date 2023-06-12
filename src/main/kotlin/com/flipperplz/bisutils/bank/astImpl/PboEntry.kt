package com.flipperplz.bisutils.bank.astImpl

import com.flipperplz.bisutils.bank.ast.IPboDirectory
import com.flipperplz.bisutils.bank.ast.IPboEntry
import com.flipperplz.bisutils.bank.ast.IPboFile
import com.flipperplz.bisutils.bank.utils.EntryMimeType

abstract class PboEntry(
        override val familyParent: IPboDirectory?,
        override val familyNode: IPboFile?
) : IPboEntry