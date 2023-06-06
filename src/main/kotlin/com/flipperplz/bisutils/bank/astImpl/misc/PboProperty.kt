package com.flipperplz.bisutils.bank.astImpl.misc

import com.flipperplz.bisutils.bank.ast.IPboFile
import com.flipperplz.bisutils.bank.ast.entry.IPboVersionEntry
import com.flipperplz.bisutils.bank.ast.misc.IPboProperty

open class PboProperty(
    override val node: IPboFile?,
    override val parent: IPboVersionEntry?,
    override val name: String,
    override val value: String
) : IPboProperty