package com.flipperplz.bisutils.bank.astImpl.mutable

import com.flipperplz.bisutils.bank.ast.mutable.IMutablePboFile
import com.flipperplz.bisutils.bank.ast.IPboEntry
import com.flipperplz.bisutils.bank.astImpl.PboEntry
import com.flipperplz.bisutils.bank.astImpl.PboFile
import com.flipperplz.bisutils.bank.options.PboDebinarizationOptions
import com.flipperplz.bisutils.bank.utils.EntryMimeType
import com.flipperplz.bisutils.family.IFamilyNode
import com.flipperplz.bisutils.io.getAsciiZ
import com.flipperplz.bisutils.io.getBytes
import com.flipperplz.bisutils.utils.IFlushable
import java.io.Closeable
import java.nio.ByteBuffer
import java.nio.charset.Charset

class MutablePboFile(
    override var defaultPrefix: String,
    override var parent: IFamilyNode?,
    override var entries: MutableList<IPboEntry>,
    override var signature: ByteArray,
): PboFile(defaultPrefix, parent, entries, signature), IMutablePboFile {

    override fun read(buffer: ByteBuffer, charset: Charset, options: PboDebinarizationOptions): Boolean {
        flush()
        entries = mutableListOf() //TODO: Read PBO
        signature = buffer.getBytes(buffer.remaining())
        return true
    }

    override fun flush() {
        super.flush()
        entries.filterIsInstance<IFlushable>().forEach { it.flush() }
        entries.filterIsInstance<Closeable>().forEach { it.close() }
        signature = byteArrayOf() //TODO: Default Signature
    }

    companion object {
        fun readMetadataBlock(pbo: MutablePboFile, buffer: ByteBuffer, charset: Charset, options: PboDebinarizationOptions): List<PboEntry> = mutableListOf<PboEntry>().apply {
            var currentEntry: PboEntry? = null

            do {
                currentEntry = readEntryMeta(pbo, buffer, charset, options)
                currentEntry?.let { add(it) }
            } while (currentEntry != null)
        }

        private fun readEntryMeta(pbo: Any, buffer: ByteBuffer, charset: Charset, options: PboDebinarizationOptions): PboEntry? {
            val start = buffer.position()
            val filename = buffer.getAsciiZ(charset)
            val mime = EntryMimeType.fromMime(buffer.getInt())
            buffer.position(start)


            TODO()
        }
    }

}