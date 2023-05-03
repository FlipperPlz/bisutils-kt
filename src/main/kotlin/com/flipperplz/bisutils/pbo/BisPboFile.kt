package com.flipperplz.bisutils.pbo

import com.flipperplz.bisutils.BisPboManager
import com.flipperplz.bisutils.pbo.io.BisPboReader
import com.flipperplz.bisutils.pbo.misc.EntryMimeType
import com.flipperplz.bisutils.utils.BisRandomAccessFile
import com.flipperplz.bisutils.utils.decompress
import com.google.common.cache.CacheBuilder
import java.io.File
import java.nio.ByteBuffer


class BisPboFile internal constructor(prefix: String) : AutoCloseable {
    internal val entries: MutableList<BisPboEntry> = mutableListOf()
    private val dataCache = CacheBuilder.newBuilder().build<BisPboDataEntry, ByteBuffer>()
    val pboEntries: List<BisPboEntry> = entries
    var pboPrefix: String = prefix
        get() = customPrefix ?: field
        internal set //TODO: if customPrefix is set we need to call set on that rather than pboPrefix itself

    val customPrefix: String?
        get() {
            for (versionEntry in entries.filterIsInstance<BisPboVersionEntry>()) {
                versionEntry.properties.lastOrNull { it.propertyName == "prefix" }?.propertyValue?.let {
                    return normalizePath(it)
                }
            }
            return null
        }
    override fun close() {
        dataCache.cleanUp()
        BisPboManager.releasePbo(this)
    }

    fun retrieveEntryData(entry: BisPboDataEntry, raw: Boolean): ByteBuffer {
        if(raw || entry.mimeType == EntryMimeType.DUMMY) return entry.entryData
        if(entry.mimeType == EntryMimeType.ENCRYPTED_DATA) throw Exception("EBO not supported.")
        if(entry.size != entry.originalSize && entry.mimeType == EntryMimeType.NORMAL_DATA) {
            return entry.entryData.decompress(entry.originalSize, true)
        }

        return entry.entryData
    }

    companion object {

        fun read(file: File, lightRead: Boolean = true, allowWrite: Boolean = false): BisPboFile {
            val reader = BisPboReader(BisRandomAccessFile(file, if (allowWrite) "rw" else "r"))

            return if(lightRead) reader.lightRead() else reader.read()
        }

        fun create(prefix: String): BisPboFile {
            val pbo = BisPboFile(prefix)
            pbo.entries.add(BisPboVersionEntry.CACHED.withPrefix(prefix))
            pbo.entries.add(BisPboDummyEntry.CACHED)
            return pbo
        }

        fun normalizePath(path: String): String {
            val result = StringBuilder(path.length)
            var separatorFlag = false
            var charsWritten = 0

            for (c in path) {
                if (c == '/' || c == '\\') {
                    if (separatorFlag) continue
                    result[charsWritten++] = '\\'
                    separatorFlag = true
                    continue
                }

                result.append(c.lowercase()).also {
                    separatorFlag = false
                    charsWritten++
                }
            }

            if (charsWritten > 0 && result[charsWritten - 1] == '\\') charsWritten--
            return result.toString()
        }
    }
}
