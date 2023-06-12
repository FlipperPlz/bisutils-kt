package com.flipperplz.bisutils.bank.astImpl.misc

import com.flipperplz.bisutils.bank.ast.IPboFile
import com.flipperplz.bisutils.bank.ast.entry.IPboVersionEntry
import com.flipperplz.bisutils.bank.ast.misc.IPboProperty

open class PboProperty(
        override val familyParent: IPboVersionEntry?,
        override val familyNode: IPboFile?,
        override val name: String,
        override val value: String
) : IPboProperty