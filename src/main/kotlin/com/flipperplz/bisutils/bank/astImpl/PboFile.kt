package com.flipperplz.bisutils.bank.astImpl

import com.flipperplz.bisutils.bank.ast.IPboEntry
import com.flipperplz.bisutils.bank.ast.IPboFile
import com.flipperplz.bisutils.bank.utils.dataEntries
import com.flipperplz.bisutils.family.interfaces.IFamilyNode
import com.flipperplz.bisutils.binarization.BisStrictDebinarizable
import com.flipperplz.bisutils.io.getBytes
import java.nio.ByteBuffer
import java.nio.charset.Charset

open class PboFile(
    override val defaultPrefix: String,
    override val parent: IFamilyNode? = null,
    override val entries: List<IPboEntry> = listOf(),
    override val signature: ByteArray,
) : BisStrictDebinarizable(), IPboFile, List<IPboEntry> by entries {
    constructor(buffer: ByteBuffer, defaultPrefix: String, parent: IFamilyNode? = null, charset: Charset = Charsets.UTF_8) : this(
        defaultPrefix,
        parent,
        mutableListOf(),
        byteArrayOf()
    ) { if(!read(buffer, charset)) throw Exception() }

    override val node: IFamilyNode?
        get() = parent?.node


    override fun read(buffer: ByteBuffer, charset: Charset): Boolean = throw Exception("Not Supported")

    override fun writeValidated(buffer: ByteBuffer, charset: Charset): Boolean {
        entries.forEach { if(!it.writeValidated(buffer, charset)) return false }
        entries.dataEntries().forEach { if(!it.writeData(buffer, charset)) return false }
        signFile(buffer)
        return true
    }

    private fun signFile(buffer: ByteBuffer) : Boolean {
        TODO("PBO SIGNATURE")
    }

}


