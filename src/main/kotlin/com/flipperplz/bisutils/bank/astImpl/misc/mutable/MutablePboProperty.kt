package com.flipperplz.bisutils.bank.astImpl.misc.mutable

import com.flipperplz.bisutils.bank.ast.IPboFile
import com.flipperplz.bisutils.bank.ast.entry.IPboVersionEntry
import com.flipperplz.bisutils.bank.ast.entry.mutable.IMutablePboVersionEntry
import com.flipperplz.bisutils.bank.ast.misc.mutable.IMutablePboProperty
import com.flipperplz.bisutils.bank.ast.mutable.IMutablePboFile
import com.flipperplz.bisutils.bank.astImpl.misc.PboProperty

class MutablePboProperty(
    override var parent: IPboVersionEntry?,
    override var node: IPboFile?,
    override var name: String = "",
    override var value: String = ""
) : PboProperty(parent, node, name, value), IMutablePboProperty