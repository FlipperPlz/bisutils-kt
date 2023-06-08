package com.flipperplz.bisutils.bank.ast.entry.mutable

import com.flipperplz.bisutils.bank.ast.IPboDirectory
import com.flipperplz.bisutils.bank.ast.IPboFile
import com.flipperplz.bisutils.bank.ast.entry.IPboVersionEntry
import com.flipperplz.bisutils.bank.ast.misc.IPboProperty
import com.flipperplz.bisutils.bank.ast.misc.mutable.IMutablePboProperty
import com.flipperplz.bisutils.bank.ast.mutable.IMutablePboDirectory
import com.flipperplz.bisutils.bank.ast.mutable.IMutablePboEntry
import com.flipperplz.bisutils.bank.ast.mutable.IMutablePboFile
import com.flipperplz.bisutils.bank.astImpl.misc.mutable.MutablePboProperty
import com.flipperplz.bisutils.bank.options.PboOptions
import com.flipperplz.bisutils.bank.utils.EntryMimeType
import java.nio.ByteBuffer

interface IMutablePboVersionEntry : IPboVersionEntry, IMutablePboEntry, Cloneable {
    override val absolutePath: String
    override val path: String
    override var children: MutableList<IMutablePboProperty>
    override var entryDecompressedSize: Long
    override var entryMime: EntryMimeType
    override var entryName: String
    override var entryOffset: Long
    override var entrySize: Long
    override var entryTimestamp: Long
    override var node: IPboFile?
    override var parent: IPboDirectory?

    override fun flush() { super.flush(); children.clear() }

    override fun read(buffer: ByteBuffer, options: PboOptions): Boolean {
        if(!super<IMutablePboEntry>.read(buffer, options)) return false

        val property = MutablePboProperty(this, node, "", "")
        while (property.read(buffer, options)) {
            if(options.removeBenignProperties && !(IPboProperty.usedProperties.contains(property.name))) continue
            children.add(property.clone())
        }

        return true
    }

    override fun clone(): IMutablePboVersionEntry = super<Cloneable>.clone() as IMutablePboVersionEntry

}