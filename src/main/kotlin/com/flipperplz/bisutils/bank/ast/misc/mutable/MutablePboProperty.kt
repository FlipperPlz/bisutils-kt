package com.flipperplz.bisutils.bank.ast.misc.mutable

import com.flipperplz.bisutils.bank.ast.misc.PboProperty
import com.flipperplz.bisutils.family.interfaces.mutable.MutableFamilyChild

interface MutablePboProperty : PboProperty, MutableFamilyChild { //TODO: KOTLIN WHY (extend PboProperty
    override var name: String
    override var value: String
}