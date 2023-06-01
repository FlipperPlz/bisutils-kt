package com.flipperplz.bisutils.bank.ast.entry.mutable

import com.flipperplz.bisutils.bank.ast.entry.PboDataEntry
import com.flipperplz.bisutils.bank.ast.mutable.MutablePboEntry
import java.nio.ByteBuffer

interface MutablePboDataEntry : MutablePboEntry, PboDataEntry {
    override val entryData: ByteBuffer
    override val children: MutableList<Any>
        get() = mutableListOf(entryData)

    override fun validateMutability(): Boolean = !super.validateMutability()

    override fun flush() {
        super.flush()
        entryData.clear()
    }
}