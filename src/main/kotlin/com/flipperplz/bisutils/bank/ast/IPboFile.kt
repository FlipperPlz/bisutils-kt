package com.flipperplz.bisutils.bank.ast

import com.flipperplz.bisutils.bank.ast.entry.IPboVersionEntry
import com.flipperplz.bisutils.bank.options.PboBinarizationOptions
import com.flipperplz.bisutils.bank.options.PboEntryBinarizationOptions
import com.flipperplz.bisutils.bank.options.PboEntryDebinarizationOptions
import com.flipperplz.bisutils.bank.utils.IPboBinaryObject
import com.flipperplz.bisutils.bank.utils.versionEntry
import com.flipperplz.bisutils.family.IFamilyMember
import com.flipperplz.bisutils.family.IFamilyNode
import com.flipperplz.bisutils.family.IFamilyParent
import java.nio.ByteBuffer
import java.nio.charset.Charset

interface IPboFile : IPboDirectory, IFamilyNode {
    override val absolutePath: String get() = prefix
    override val path: String get() = ""
    override val children: List<IPboVFSEntry>?
    override val parent: IPboDirectory? get() = null
    override val entryName: String get() = prefix
    override val node: IPboFile? get() = null
    override val directories: List<IPboDirectory>
    override val entries: List<IPboEntry>
    val prefix: String get() = /*entries.versionEntry()?.getProperty("prefix")?.value ?:*/ defaultPrefix /*TODO: DIG THROUGH VERSION ENTRY*/
    val defaultPrefix: String
    val signature: ByteArray //should always be 20 bytes **IIRC**

    override fun calculateBinaryLength(options: PboEntryBinarizationOptions?): Long {
        return super.calculateBinaryLength(options) //TODO: ADD SIGNATURE LENGTH AND DATA LENGTH
    }

    override fun writeValidated(buffer: ByteBuffer, options: PboEntryBinarizationOptions?): Boolean {
        if(!super.writeValidated(buffer, options)) return false
        return writeSignature(buffer, options)
    }

    override fun read(buffer: ByteBuffer, options: PboEntryDebinarizationOptions): Boolean =
        throw Exception("Not Supported!")

    private fun writeSignature(buffer: ByteBuffer, options: PboBinarizationOptions?) : Boolean {
        TODO("PBO SIGNATURE")
    }

    override fun isValid(): Boolean {
        if(!super.isValid()) return false
        if(entries.firstOrNull() !is IPboVersionEntry) return false
        TODO()
    }

}