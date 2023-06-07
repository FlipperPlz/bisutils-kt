package com.flipperplz.bisutils.bank.astImpl.misc.mutable

import com.flipperplz.bisutils.bank.ast.entry.mutable.IMutablePboVersionEntry
import com.flipperplz.bisutils.bank.ast.misc.mutable.IMutablePboProperty
import com.flipperplz.bisutils.bank.ast.mutable.IMutablePboFile
import com.flipperplz.bisutils.bank.astImpl.misc.PboProperty

class MutablePboProperty(
    override var node: IMutablePboFile?,
    override var parent: IMutablePboVersionEntry?,
    override var name: String,
    override var value: String
) : PboProperty(node, parent, name, value), IMutablePboProperty