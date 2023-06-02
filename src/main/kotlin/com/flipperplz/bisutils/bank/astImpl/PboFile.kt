package com.flipperplz.bisutils.bank.astImpl

import com.flipperplz.bisutils.bank.ast.IPboEntry
import com.flipperplz.bisutils.bank.ast.IPboFile
import com.flipperplz.bisutils.bank.options.PboBinarizationOptions
import com.flipperplz.bisutils.bank.options.PboDebinarizationOptions
import com.flipperplz.bisutils.bank.utils.IPboBinaryObject
import com.flipperplz.bisutils.bank.utils.dataEntries
import com.flipperplz.bisutils.family.IFamilyNode
import java.nio.ByteBuffer
import java.nio.charset.Charset

open class PboFile(
    override val defaultPrefix: String,
    override val parent: IFamilyNode? = null,
    override val entries: List<IPboEntry> = listOf(),
    override val signature: ByteArray,
) : IPboBinaryObject, IPboFile, List<IPboEntry> by entries {
    override val node: IFamilyNode?
        get() = parent?.node

    override fun read(buffer: ByteBuffer, charset: Charset, options: PboDebinarizationOptions): Boolean =
        throw Exception("Not Supported")

    override fun writeValidated(buffer: ByteBuffer, charset: Charset, options: PboBinarizationOptions?): Boolean {
        entries.forEach { if(!it.writeValidated(buffer, charset, options)) return false }
        entries.dataEntries().forEach { if(!it.writeData(buffer, charset, options)) return false }
        return signFile(buffer, charset, options)
    }

    private fun signFile(buffer: ByteBuffer, charset: Charset, options: PboBinarizationOptions?) : Boolean {
        TODO("PBO SIGNATURE")
    }

}


