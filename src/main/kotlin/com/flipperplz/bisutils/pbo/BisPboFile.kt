package com.flipperplz.bisutils.pbo

import com.flipperplz.bisutils.BisPboManager
import com.flipperplz.bisutils.utils.BisRandomAccessFile
import com.google.common.cache.CacheBuilder
import java.io.File

class BisPboFile private constructor() : AutoCloseable {
    lateinit var entries: List<BisPboEntry>
        private set;

    private val dataCache = CacheBuilder.newBuilder().build<BisPboDataEntry, ByteArray>()


    @Suppress("UNCHECKED_CAST")
    private fun <T> getCachedEntry(entry: BisPboDataEntry): T? = dataCache.getIfPresent(entry) as? T

    fun <T> getOrCreateCachedEntry(entry: BisPboDataEntry, file: File? = null, managePbo: Boolean = true): T? {
        return getCachedEntry(entry) ?: TODO("Entry cache creation not implemented")
    }



    companion object {
        fun lightRead(file: File) {
            val result = BisPboFile()
            val buffer = BisRandomAccessFile(file, "r")

            BisPboManager.managePbo(result, buffer)


        }
    }

    override fun close() {
        dataCache.cleanUp()
        BisPboManager.releasePbo(this)
    }
}
