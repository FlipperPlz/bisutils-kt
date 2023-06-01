package com.flipperplz.bisutils.utils

interface BisMutableFamily : BisFamily {
    override val children: MutableList<Any>?
    override var parent: BisFamily?
}