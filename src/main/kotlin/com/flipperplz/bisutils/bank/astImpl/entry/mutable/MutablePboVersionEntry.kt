package com.flipperplz.bisutils.bank.astImpl.entry.mutable

import com.flipperplz.bisutils.bank.ast.IPboFile
import com.flipperplz.bisutils.bank.ast.entry.mutable.IMutablePboVersionEntry
import com.flipperplz.bisutils.bank.ast.misc.IPboProperty
import com.flipperplz.bisutils.bank.ast.misc.mutable.IMutablePboProperty
import com.flipperplz.bisutils.bank.astImpl.misc.mutable.MutablePboProperty
import com.flipperplz.bisutils.bank.astImpl.mutable.MutablePboEntry
import com.flipperplz.bisutils.bank.options.PboDebinarizationOptions
import com.flipperplz.bisutils.bank.utils.EntryMimeType
import com.flipperplz.bisutils.family.IFamilyNode
import com.flipperplz.bisutils.io.getAsciiZ
import java.nio.ByteBuffer
import java.nio.charset.Charset

class MutablePboVersionEntry(
    lowestBranch: IPboFile?,
    parent: IFamilyNode?,
    entryName: String,
    entryMime: EntryMimeType,
    entryDecompressedSize: Long,
    entryOffset: Long,
    entryTimestamp: Long,
    entrySize: Long,
    override var properties: MutableList<IMutablePboProperty>,
) : MutablePboEntry(lowestBranch, parent, entryName, entryMime, entryDecompressedSize, entryOffset, entryTimestamp, entrySize), IMutablePboVersionEntry {

    companion object {
        private fun <T: IPboProperty> ByteBuffer.readPboProperties(charset: Charset, createProperty: (String, String) -> T): List<T> =
            mutableListOf<T>().apply {
                var propertyName: String
                while (getAsciiZ(charset).also { propertyName = it }.isNotEmpty()) {
                    val propertyValue: String = getAsciiZ(charset)
                    add(createProperty(propertyName, propertyValue))
                }
            }
    }

    override fun read(buffer: ByteBuffer, charset: Charset, options: PboDebinarizationOptions): Boolean {
        if(!super.read(buffer, charset, options)) return false
        if(entryMime != EntryMimeType.VERSION) return false
        properties = buffer.readPboProperties(charset) {  name, value ->
            MutablePboProperty(this, node, name, value)
        }.toMutableList()

        return true
    }
}