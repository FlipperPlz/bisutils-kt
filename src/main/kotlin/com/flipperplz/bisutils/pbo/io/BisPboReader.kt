package com.flipperplz.bisutils.pbo.io

import com.flipperplz.bisutils.BisPboManager
import com.flipperplz.bisutils.pbo.*
import com.flipperplz.bisutils.pbo.misc.BisPboProperty
import com.flipperplz.bisutils.pbo.misc.EntryMimeType
import com.flipperplz.bisutils.pbo.misc.StagedPboDataEntry
import com.flipperplz.bisutils.utils.BisRandomAccessFile
import com.flipperplz.bisutils.utils.readAsciiZ
import com.flipperplz.bisutils.utils.readBytes
import com.flipperplz.bisutils.utils.readInt32
import java.io.File
import java.nio.ByteBuffer
import kotlin.math.abs

class BisPboReader(internal val buffer: BisRandomAccessFile) : AutoCloseable {
    constructor(file: File) : this(BisRandomAccessFile(file, "r"))

    private val pos: Long
        get() = buffer.filePointer
    private var flagPtr: Long? = null;

    fun lightRead(): BisPboFile {
        val result = BisPboFile(buffer.fileName)
        BisPboManager.managePbo(result, this)

        return initializeFile(result)
    }

    fun read(): BisPboFile {
        val pbo = lightRead()
        val entries = pbo.entries

        for ((i, entry) in entries.withIndex()) {

            if(entry !is StagedPboDataEntry || (entry.dataOffset ?: -1) <= 0) continue
            val reader = entry.stageBuffer
            val startPtr = reader.filePointer

            reader.seek(entry.dataOffset!!)
            entries[i] = BisPboDataEntry.CACHED(
                pbo,
                entry.fileName,
                entry.offset,
                entry.timeStamp,
                entry.mimeType,
                entry.originalSize,
                entry.size,
                ByteBuffer.wrap(reader.readBytes(entry.size))
            )
            reader.seek(startPtr)
        }

        return pbo
    }

    private fun initializeFile(pbo: BisPboFile): BisPboFile = pbo.apply {
        entries.addAll(readMetaBlock(pbo))
        flagPtr = null
        initializeOffsets(entries)
    }

    private fun initializeOffsets(entries: List<BisPboEntry>) = entries.filterIsInstance<BisPboDataEntry>().takeIf { it.isNotEmpty() }?.fold(buffer) { reader, entry ->
        if (entry is StagedPboDataEntry) entry.dataOffset = reader.filePointer
        reader.skipBytes(entry.size)

        reader
    }

    private fun readMetaBlock(pbo: BisPboFile): MutableList<BisPboEntry> {
        val entryList: MutableList<BisPboEntry> = mutableListOf()
        var currentEntry: BisPboEntry? = null

        do {
            val read = readEntryMeta(pbo)
            if(read == null) {
                if(currentEntry == null || flagPtr == null) throw Exception("Failed to read entry")
                if(currentEntry !is BisPboDummyEntry)  throw Exception("Failed to recover")
                currentEntry = BisPboDataEntry.INFILE(pbo, buffer, flagPtr!!, "", 0, 0, EntryMimeType.DUMMY, 0, 0)
                entryList.add(currentEntry)
                buffer.seek(flagPtr!!)
                break
            }

            currentEntry = read
            currentEntry.let { entryList.add(it) }
        } while (currentEntry !is BisPboDummyEntry)

        return entryList
    }

    private fun readEntryMeta(pbo: BisPboFile): BisPboEntry? {
        flagPtr = pos

        val entryName = buffer.readAsciiZ()
        val parsedMime = EntryMimeType.fromMime(buffer.readInt32()) ?: return null
        val originalSize = abs(buffer.readInt32())
        val offset = abs(buffer.readInt32())
        val timestamp = buffer.readInt32()
        var packedSize = abs(buffer.readInt32())

        if(packedSize >= buffer.length()) packedSize = 0

        if(entryName == "" && originalSize == 0 && offset == 0 && timestamp == 0 && packedSize == 0) {
            if(parsedMime == EntryMimeType.VERSION) return BisPboVersionEntry.INFILE(pbo, buffer, flagPtr!!, readPboProperties(pbo)).apply {
                properties.forEach { it.owner = this }
            }
            if(parsedMime == EntryMimeType.DUMMY) return BisPboDummyEntry.INFILE(pbo, buffer, flagPtr!!)
        }

        return BisPboDataEntry.INFILE(pbo, buffer, flagPtr!!, entryName, offset, timestamp, parsedMime, originalSize, packedSize)
    }

    private fun readPboProperties(pbo: BisPboFile): MutableList<BisPboProperty> {
        val properties = mutableListOf<BisPboProperty>()

        var propertyName: String
        while (buffer.readAsciiZ().also { propertyName = it }.isNotEmpty()) {
            val propertyValue: String = buffer.readAsciiZ()
            if(propertyName.lowercase() == "prefix") BisPboFile.normalizePath(propertyValue)?.let {
                pbo.pboPrefix = it
            }
            properties.add(BisPboProperty(propertyName, propertyValue))
        }

        return properties
    }

    override fun close() {
        buffer.close()
    }
}

