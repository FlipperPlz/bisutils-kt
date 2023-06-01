package com.flipperplz.bisutils.bank.astImpl

import com.flipperplz.bisutils.bank.ast.PboEntry
import com.flipperplz.bisutils.bank.ast.PboFile
import com.flipperplz.bisutils.family.interfaces.FamilyNode
import com.flipperplz.bisutils.binarization.BisStrictDebinarizable
import com.flipperplz.bisutils.io.getBytes
import java.nio.ByteBuffer
import java.nio.charset.Charset

open class PboFileImpl(
    override val defaultPrefix: String,
    override val entries: List<PboEntry> = emptyList(),
    override val parent: FamilyNode?
) : BisStrictDebinarizable(), PboFile, List<PboEntry> by entries {
    override val node: FamilyNode?
        get() = parent?.node
    override lateinit var signature: ByteArray


    override fun read(buffer: ByteBuffer, charset: Charset) { //TODO: return boolean
        //TODO("Not yet implemented")

        signature = buffer.getBytes(20)
    }

    override fun writeValidated(buffer: ByteBuffer, charset: Charset): Boolean {
        entries.forEach { if(!it.writeValidated(buffer, charset)) return false }
        //TODO: Write entry data
        signFile(buffer)
        return true
    }

    private fun signFile(buffer: ByteBuffer) : Boolean {
        TODO("PBO SIGNATURE")
    }

}


