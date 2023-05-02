package com.flipperplz.bisutils.pbo

import com.flipperplz.bisutils.BisPboManager
import com.google.common.cache.CacheBuilder
import java.io.File
import java.nio.ByteBuffer

class BisPboFile internal constructor() : AutoCloseable {
    internal lateinit var entries: MutableList<BisPboEntry>
    private val dataCache = CacheBuilder.newBuilder().build<BisPboDataEntry, ByteBuffer>()
    val pboEntries: List<BisPboEntry> = entries


    @Suppress("UNCHECKED_CAST")
    private fun <T> getCachedEntry(entry: BisPboDataEntry): T? = dataCache.getIfPresent(entry) as? T

    fun <T> getOrCreateCachedEntry(entry: BisPboDataEntry, file: File? = null, managePbo: Boolean = true): T? {
        return getCachedEntry(entry)!!
    }

    override fun close() {
        dataCache.cleanUp()
        BisPboManager.releasePbo(this)
    }
}
