package com.flipperplz.bisutils.bank.ast.entry

import com.flipperplz.bisutils.bank.ast.IPboEntry
import com.flipperplz.bisutils.bank.options.PboBinarizationOptions
import java.nio.ByteBuffer
import java.nio.charset.Charset

interface IPboDataEntry : IPboEntry {
    val entryData: ByteBuffer

    override fun isValid(): Boolean = entrySize == entryData.capacity().toLong()

    fun validateMutability(): Boolean = entryData.isReadOnly

    fun writeDataValidated(buffer: ByteBuffer, charset: Charset, options: PboBinarizationOptions?): Boolean {
        entryData.put(buffer)
        return true
    }

    fun writeData(buffer: ByteBuffer, charset: Charset, options: PboBinarizationOptions?): Boolean =
        if(!isValid()) writeValidated(buffer, charset, options) else false

}