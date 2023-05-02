package com.flipperplz.bisutils.pbo

import com.flipperplz.bisutils.BisPboManager
import com.flipperplz.bisutils.pbo.io.BisPboReader
import com.flipperplz.bisutils.pbo.misc.StagedPboDataEntry
import com.flipperplz.bisutils.pbo.misc.StagedPboEntry
import com.flipperplz.bisutils.utils.BisRandomAccessFile
import com.flipperplz.bisutils.utils.readBytes
import com.google.common.cache.CacheBuilder
import java.io.File
import java.nio.ByteBuffer
import kotlin.time.Duration.Companion.seconds

class BisPboFile private constructor() : AutoCloseable {
    lateinit var entries: MutableList<BisPboEntry>
        private set;

    private val dataCache = CacheBuilder.newBuilder().build<BisPboDataEntry, ByteBuffer>()


    @Suppress("UNCHECKED_CAST")
    private fun <T> getCachedEntry(entry: BisPboDataEntry): T? = dataCache.getIfPresent(entry) as? T

    fun <T> getOrCreateCachedEntry(entry: BisPboDataEntry, file: File? = null, managePbo: Boolean = true): T? {
        return getCachedEntry(entry)

    }



    companion object {
        fun lightRead(file: File): BisPboFile {
            val result = BisPboFile()
            val reader = BisPboReader(file)
            BisPboManager.managePbo(result, reader)

            return result.apply {
                entries = reader.readMetaBlock()
                reader.initializeOffsets(entries)
            }
        }

        fun read(file: File): BisPboFile {
            val pbo = lightRead(file)
            val entries = pbo.entries

            for ((i, entry) in entries.withIndex()) {

                if(entry !is StagedPboDataEntry || (entry.dataOffset ?: -1) <= 0) continue
                val reader = entry.stageBuffer
                val startPtr = reader.filePointer

                reader.seek(entry.dataOffset!!)
                entries[i] = BisPboDataEntry.CACHED(
                    entry.fileName,
                    entry.offset,
                    entry.timeStamp,
                    entry.mimeType,
                    entry.originalSize,
                    ByteBuffer.wrap(reader.readBytes(entry.size))
                )
            }

            return pbo
        }
    }

    override fun close() {
        dataCache.cleanUp()
        BisPboManager.releasePbo(this)
    }
}
