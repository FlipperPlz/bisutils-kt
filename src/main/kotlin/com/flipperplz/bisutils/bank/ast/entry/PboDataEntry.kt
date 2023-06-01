package com.flipperplz.bisutils.bank.ast.entry

import com.flipperplz.bisutils.bank.ast.PboEntry
import java.nio.ByteBuffer

interface PboDataEntry : PboEntry {
    val entryData: ByteBuffer

    override fun isValid(): Boolean = entrySize == entryData.capacity().toLong()

    fun validateMutability(): Boolean = entryData.isReadOnly
}