package com.flipperplz.bisutils.pbo

import com.flipperplz.bisutils.BisPboManager
import com.flipperplz.bisutils.pbo.io.BisPboReader
import com.flipperplz.bisutils.utils.BisRandomAccessFile
import com.google.common.cache.CacheBuilder
import java.io.File
import java.nio.ByteBuffer


class BisPboFile internal constructor() : AutoCloseable {
    internal val entries: MutableList<BisPboEntry> = mutableListOf()
    private val dataCache = CacheBuilder.newBuilder().build<BisPboDataEntry, ByteBuffer>()
    val pboEntries: List<BisPboEntry> = entries

    override fun close() {
        dataCache.cleanUp()
        BisPboManager.releasePbo(this)
    }

    fun retrieveEntryData(entry: BisPboDataEntry, raw: Boolean): ByteBuffer {
        if(raw) return entry.entryData

        TODO()
    }

    companion object {
        operator fun invoke(file: File, lightRead: Boolean = true, mode: String = "r"): BisPboFile =
            read(file, lightRead, mode)

        fun read(file: File, lightRead: Boolean = true, mode: String = "r"): BisPboFile {
            val reader = BisPboReader(BisRandomAccessFile(file, mode))

            return if(lightRead) reader.lightRead() else reader.read()
        }

        fun create(prefix: String): BisPboFile {
            val pbo = BisPboFile()
            pbo.entries.add(BisPboVersionEntry.CACHED.withPrefix(prefix))
            pbo.entries.add(BisPboDummyEntry.CACHED)
            return pbo
        }
    }
}
