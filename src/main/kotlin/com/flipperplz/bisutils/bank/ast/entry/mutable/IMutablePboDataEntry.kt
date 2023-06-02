package com.flipperplz.bisutils.bank.ast.entry.mutable

import com.flipperplz.bisutils.bank.ast.entry.IPboDataEntry
import com.flipperplz.bisutils.bank.ast.mutable.IMutablePboEntry
import java.nio.ByteBuffer

interface IMutablePboDataEntry : IMutablePboEntry, IPboDataEntry {
    override var entryData: ByteBuffer

    override fun validateMutability(): Boolean = !super.validateMutability()

    override fun flush() {
        super.flush()
        entryData.clear()
    }
}