package com.flipperplz.bisutils.bank.astImpl.mutable

import com.flipperplz.bisutils.bank.ast.mutable.IMutablePboFile
import com.flipperplz.bisutils.bank.ast.IPboEntry
import com.flipperplz.bisutils.bank.astImpl.PboEntry
import com.flipperplz.bisutils.bank.astImpl.PboFile
import com.flipperplz.bisutils.bank.astImpl.entry.PboVersionEntry
import com.flipperplz.bisutils.bank.astImpl.entry.mutable.MutablePboVersionEntry
import com.flipperplz.bisutils.bank.astImpl.misc.mutable.MutablePboProperty
import com.flipperplz.bisutils.bank.options.PboDebinarizationOptions
import com.flipperplz.bisutils.bank.utils.BankPathUtilities
import com.flipperplz.bisutils.bank.utils.EntryMimeType
import com.flipperplz.bisutils.family.IFamilyNode
import com.flipperplz.bisutils.io.getAsciiZ
import com.flipperplz.bisutils.io.getBytes
import com.flipperplz.bisutils.utils.IFlushable
import java.io.Closeable
import java.nio.ByteBuffer
import java.nio.charset.Charset
import kotlin.math.abs

class MutablePboFile(
    override var defaultPrefix: String,
    override var parent: IFamilyNode?,
    override var entries: MutableList<IPboEntry>,
    override var signature: ByteArray,
): PboFile(defaultPrefix, parent, entries, signature), IMutablePboFile {

    override fun read(buffer: ByteBuffer, charset: Charset, options: PboDebinarizationOptions): Boolean {
        flush()
        entries.addAll(readMetadataBlock(this, buffer, charset, options))
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

        private fun readEntryMeta(pbo: MutablePboFile, buffer: ByteBuffer, charset: Charset, options: PboDebinarizationOptions): PboEntry? {
            var filename = buffer.getAsciiZ(charset)
            val mime = EntryMimeType.fromMime(buffer.getInt())
            val decompressedSize = abs(buffer.getLong())
            val offset = abs(buffer.getLong())
            val timestamp = abs(buffer.getLong())
            val size = abs(buffer.getLong())
            if(
                filename == "" &&
                mime == EntryMimeType.DUMMY &&
                decompressedSize == 0L &&
                offset == 0L &&
                timestamp == 0L &&
                size == 0L
            ) return null
            return when(mime) {
                EntryMimeType.VERSION -> MutablePboVersionEntry(
                    pbo,
                    pbo,
                    filename,
                    mime,
                    decompressedSize,
                    offset,
                    timestamp,
                    size,
                    mutableListOf()
                ).apply {
                    properties.addAll(
                        MutablePboVersionEntry.readPboProperties(buffer, charset) { name, value ->
                            MutablePboProperty(this, pbo, name, value) //TODO: correctly Identify Parent Directory
                        }
                    )
                }
                else -> {
                    val path = BankPathUtilities.normalizePath(filename).split('\\')
                    val directory = path.dropLast(1).joinToString("\\")
                    filename = path.lastOrNull() ?: ""
                    TODO()
                }
            }
        }
    }

}