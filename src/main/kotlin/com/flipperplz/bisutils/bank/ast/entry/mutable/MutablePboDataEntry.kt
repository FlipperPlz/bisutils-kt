package com.flipperplz.bisutils.bank.ast.entry.mutable

import com.flipperplz.bisutils.bank.ast.entry.PboDataEntry
import com.flipperplz.bisutils.bank.ast.mutable.MutablePboEntry
import java.nio.ByteBuffer

interface MutablePboDataEntry : MutablePboEntry, PboDataEntry {
    override var entryData: ByteBuffer

    override fun validateMutability(): Boolean = !super.validateMutability()

    override fun flush() {
        super.flush()
        entryData.clear()
    }
}