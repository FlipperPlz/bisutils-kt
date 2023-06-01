package com.flipperplz.bisutils.bank.ast

import com.flipperplz.bisutils.bank.ast.misc.PboElement
import com.flipperplz.bisutils.utils.BisFamily
import com.flipperplz.bisutils.utils.BisStrictBinarizable

interface PboFile : List<PboEntry>, PboElement, BisStrictBinarizable {
    val defaultPrefix: String
    override val parent: BisFamily?
        get() = null
    override val children: List<Any>?
        get() = entries
    override val size: Int
        get() = entries.size
    val entries: List<PboEntry>
    override val binaryLength: Long
        get() = TODO()//TODO: calculate length

    override fun isValid(): Boolean {
        if(!entries.all { it.isValid() }) return false
        //TODO check signature
        return true
    }

    var signature: ByteArray //should always be 20 bytes **IIRC**

}