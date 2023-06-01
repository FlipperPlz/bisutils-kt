package com.flipperplz.bisutils.bank.ast

import com.flipperplz.bisutils.bank.ast.misc.MutablePboElement
import com.flipperplz.bisutils.utils.BisFamily
import com.flipperplz.bisutils.utils.BisFlushable

interface MutablePboFile : MutablePboElement, PboFile, BisFlushable {
    override val entries: MutableList<MutablePboEntry>
    override var signature: MutableList<Byte>


    //TODO: wtf kotlin, you're killing me
    override var parent: BisFamily?
    override val children: MutableList<Any>?
        get() = entries as MutableList<Any>


    override fun flush() {
        entries.filterIsInstance<BisFlushable>().forEach { it.flush() }
        signature.clear()
    }
}