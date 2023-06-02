package com.flipperplz.bisutils.bank.ast.misc.mutable

import com.flipperplz.bisutils.bank.ast.misc.IPboProperty
import com.flipperplz.bisutils.family.mutable.IMutableFamilyChild

interface IMutablePboProperty : IPboProperty, IMutableFamilyChild {
    override var name: String
    override var value: String
}