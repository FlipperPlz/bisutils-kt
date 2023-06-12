package com.flipperplz.bisutils.bank.ast.entry.mutable

import com.flipperplz.bisutils.bank.ast.IPboDirectory
import com.flipperplz.bisutils.bank.ast.IPboFile
import com.flipperplz.bisutils.bank.ast.entry.IPboVersionEntry
import com.flipperplz.bisutils.bank.ast.misc.IPboProperty
import com.flipperplz.bisutils.bank.ast.misc.mutable.IMutablePboProperty
import com.flipperplz.bisutils.bank.ast.mutable.IMutablePboEntry
import com.flipperplz.bisutils.bank.astImpl.misc.mutable.MutablePboProperty
import com.flipperplz.bisutils.bank.options.PboOptions
import com.flipperplz.bisutils.bank.utils.EntryMimeType
import java.nio.ByteBuffer

interface IMutablePboVersionEntry : IPboVersionEntry, IMutablePboEntry, Cloneable {
    override val absolutePath: String
    override val path: String
    override var familyChildren: MutableList<IMutablePboProperty>
    override var entryDecompressedSize: Long
    override var entryMime: EntryMimeType
    override var entryName: String
    override var entryOffset: Long
    override var entrySize: Long
    override var entryTimestamp: Long
    override var familyNode: IPboFile?
    override var familyParent: IPboDirectory?

    override fun flush() { super.flush(); familyChildren.clear() }

    override fun read(buffer: ByteBuffer, options: PboOptions): Boolean {
        if(!super<IMutablePboEntry>.read(buffer, options)) return false

        val property = MutablePboProperty(this, familyNode, "", "")
        while (property.read(buffer, options)) {
            if(options.removeBenignProperties && !(IPboProperty.usedProperties.contains(property.name))) continue
            familyChildren.add(property.clone())
        }

        return true
    }

    override fun clone(): IMutablePboVersionEntry = super<Cloneable>.clone() as IMutablePboVersionEntry

}