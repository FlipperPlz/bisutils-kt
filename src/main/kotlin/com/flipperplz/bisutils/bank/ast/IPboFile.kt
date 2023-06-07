package com.flipperplz.bisutils.bank.ast

import com.flipperplz.bisutils.bank.ast.entry.IPboVersionEntry
import com.flipperplz.bisutils.bank.options.PboBinarizationOptions
import com.flipperplz.bisutils.bank.options.PboEntryBinarizationOptions
import com.flipperplz.bisutils.bank.options.PboEntryDebinarizationOptions
import com.flipperplz.bisutils.family.IFamilyNode
import com.flipperplz.bisutils.options.BisOptions
import java.nio.ByteBuffer

interface IPboFile : IPboDirectory, IFamilyNode, Cloneable {
    override val absolutePath: String get() = prefix
    override val path: String get() = ""
    override val children: List<IPboVFSEntry>?
    override val parent: IPboDirectory? get() = null
    override val entryName: String get() = prefix
    override val node: IPboFile? get() = null

    override val directories: List<IPboDirectory> get() = super.directories
    override val entries: List<IPboEntry> get() = super.entries
    val prefix: String get() = /*entries.versionEntry()?.getProperty("prefix")?.value ?:*/ defaultPrefix /*TODO: DIG THROUGH VERSION ENTRY*/
    val defaultPrefix: String
    val signature: ByteArray //should always be 20 bytes **IIRC**

    override fun calculateBinaryLength(options: PboEntryBinarizationOptions?): Long {
        return super.calculateBinaryLength(options) //TODO: ADD SIGNATURE LENGTH AND DATA LENGTH
    }

    override fun writeValidated(buffer: ByteBuffer, options: PboEntryBinarizationOptions?): Boolean {
        if(!super.writeValidated(buffer, options)) return false
        buffer.put(signature)
        return true
    }

    override fun read(buffer: ByteBuffer, options: PboEntryDebinarizationOptions): Boolean =
        throw Exception("Not Supported!")

    override fun isValid(options: BisOptions?): Boolean {
        if(!super.isValid(options)) return false
        if(entries.firstOrNull() !is IPboVersionEntry) return false
        return children != null && calculateSignature(children!!, null).contentEquals(signature)
    }

    public override fun clone(): IPboFile = super<Cloneable>.clone() as IPboFile


    companion object {
        fun calculateSignature(entries: List<IPboVFSEntry>, options: PboEntryBinarizationOptions?): ByteArray {
            TODO()
        }
    }

}