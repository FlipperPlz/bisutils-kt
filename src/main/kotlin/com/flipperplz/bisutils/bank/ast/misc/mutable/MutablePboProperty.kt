package com.flipperplz.bisutils.bank.ast.misc.mutable

import com.flipperplz.bisutils.bank.ast.misc.PboProperty

interface MutablePboProperty : PboProperty, MutablePboElement {
    override var name: String
    override var value: String
    override val children: MutableList<Any>
        get() = mutableListOf(name, value)
}