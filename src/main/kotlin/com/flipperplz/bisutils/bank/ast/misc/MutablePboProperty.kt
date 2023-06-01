package com.flipperplz.bisutils.bank.ast.misc

interface MutablePboProperty : PboProperty, MutablePboElement {
    override var name: String
    override var value: String
    override val children: MutableList<Any>
        get() = mutableListOf(name, value)
}