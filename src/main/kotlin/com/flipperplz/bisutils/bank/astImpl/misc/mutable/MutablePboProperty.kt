package com.flipperplz.bisutils.bank.astImpl.misc.mutable

import com.flipperplz.bisutils.bank.ast.IPboFile
import com.flipperplz.bisutils.bank.ast.entry.IPboVersionEntry
import com.flipperplz.bisutils.bank.ast.misc.mutable.IMutablePboProperty
import com.flipperplz.bisutils.bank.astImpl.misc.PboProperty

class MutablePboProperty(
        override var familyParent: IPboVersionEntry?,
        override var familyNode: IPboFile?,
        override var name: String = "",
        override var value: String = ""
) : PboProperty(familyParent, familyNode, name, value), IMutablePboProperty