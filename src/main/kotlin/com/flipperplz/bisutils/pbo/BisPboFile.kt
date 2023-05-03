package com.flipperplz.bisutils.pbo

import com.flipperplz.bisutils.BisPboManager
import com.google.common.cache.CacheBuilder
import java.io.File
import java.io.FileOutputStream
import java.nio.ByteBuffer
import java.nio.file.Path


class BisPboFile internal constructor() : AutoCloseable {
    internal val entries: MutableList<BisPboEntry> = mutableListOf()
    private val dataCache = CacheBuilder.newBuilder().build<BisPboDataEntry, ByteBuffer>()
    val pboEntries: List<BisPboEntry> = entries

    override fun close() {
        dataCache.cleanUp()
        BisPboManager.releasePbo(this)
    }

}
