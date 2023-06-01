package com.flipperplz.bisutils.bank.ast

import com.flipperplz.bisutils.bank.ast.misc.PboElement
import com.flipperplz.bisutils.utils.BisFamily

interface PboFile : List<PboEntry>, PboElement {
    val defaultPrefix: String
    override val parent: BisFamily?
        get() = null
    override val children: List<Any>?
        get() = entries
    val entries: List<PboEntry>
    val signature: List<Byte> //should always be 20 bytes **IIRC**
}