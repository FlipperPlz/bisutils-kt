package com.flipperplz.bisutils.bank.ast.entry

import com.flipperplz.bisutils.bank.ast.IPboEntry
import java.nio.ByteBuffer

interface IPboDataEntry : IPboEntry {
    val entryData: ByteBuffer

    override fun isValid(): Boolean = entrySize == entryData.capacity().toLong()

    fun validateMutability(): Boolean = entryData.isReadOnly
}