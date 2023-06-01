package com.flipperplz.bisutils.bank.ast.mutable

import com.flipperplz.bisutils.bank.ast.PboEntry
import com.flipperplz.bisutils.bank.ast.PboFile
import com.flipperplz.bisutils.bank.ast.misc.mutable.MutablePboElement
import com.flipperplz.bisutils.utils.BisFamily
import com.flipperplz.bisutils.utils.BisFlushable

interface MutablePboFile : MutablePboElement, PboFile, BisFlushable {
    override val entries: MutableList<PboEntry>
    override var signature: ByteArray

    //TODO: wtf kotlin, you're killing me
    override var parent: BisFamily?
    override val children: MutableList<Any>?
        get() = entries as MutableList<Any>


    override fun flush() {
        entries.filterIsInstance<BisFlushable>().forEach { it.flush() }
        signature = byteArrayOf()
    }
}