package com.flipperplz.bisutils.bank.io

import com.flipperplz.bisutils.BisPboManager
import com.flipperplz.bisutils.bank.*
import com.flipperplz.bisutils.bank.utils.EntryMimeType
import com.flipperplz.bisutils.bank.utils.StagedPboDataEntry
import com.flipperplz.bisutils.utils.*
import java.io.File
import java.nio.ByteBuffer
import kotlin.math.abs

class BisPboReader(internal val buffer: BisRandomAccessFile) : AutoCloseable {
    constructor(file: File) : this(BisRandomAccessFile(file, "r"))

    private val pos: Long
        get() = buffer.filePointer
    private var flagPtr: Long? = null

    fun lightRead(): PboFile {
        val result = PboFile(buffer.fileName)
        BisPboManager.managePbo(result, this)

        return initializeFile(result)
    }

    fun read(): PboFile {
        val pbo = lightRead()
        val entries = pbo.entries

        for ((i, entry) in entries.withIndex()) {

            if (entry !is StagedPboDataEntry || (entry.dataOffset ?: -1) <= 0) continue
            val reader = entry.stageBuffer
            val startPtr = reader.filePointer

            reader.seek(entry.dataOffset!!)
            entries[i] = PboDataEntry.CACHED(
                pbo,
                entry.fileName,
                entry.offset,
                entry.timeStamp,
                entry.mimeType,
                entry.originalSize,
                entry.size,
                ByteBuffer.wrap(reader.readBytes(entry.size.toInt()))
            )
            reader.seek(startPtr)
        }

        return pbo
    }

    private fun initializeFile(pbo: PboFile): PboFile = pbo.apply {
        entries.addAll(readMetaBlock(pbo))
        flagPtr = null
        initializeOffsets(entries)
    }

    private fun initializeOffsets(entries: List<PboEntry>) =
        entries.filterIsInstance<PboDataEntry>().takeIf { it.isNotEmpty() }?.fold(buffer) { reader, entry ->
            if (entry is StagedPboDataEntry) entry.dataOffset = reader.filePointer
            reader.skipBytes(entry.size.toInt())

            reader
        }

    private fun readMetaBlock(pbo: PboFile): MutableList<PboEntry> {
        val entryList: MutableList<PboEntry> = mutableListOf()
        var currentEntry: PboEntry? = null

        do {
            val read = readEntryMeta(pbo)
            if (read == null) {
                if (currentEntry == null || flagPtr == null) throw Exception("Failed to read entry")
                if (currentEntry !is PboDummyEntry) throw Exception("Failed to recover")
                currentEntry = PboDataEntry.INFILE(pbo, buffer, flagPtr!!, "", 0, 0, EntryMimeType.DUMMY, 0, 0)
                entryList.add(currentEntry)
                buffer.seek(flagPtr!!)
                break
            }

            currentEntry = read
            currentEntry.let { entryList.add(it) }
        } while (currentEntry !is PboDummyEntry)

        return entryList
    }

    private fun readEntryMeta(pbo: PboFile): PboEntry? {
        flagPtr = pos

        val entryName = buffer.readAsciiZ()
        val parsedMime = EntryMimeType.fromMime(buffer.readInt32()) ?: return null
        val originalSize = abs(buffer.readLong32())
        val offset = abs(buffer.readLong32())
        val timestamp = buffer.readLong32()
        var packedSize = abs(buffer.readLong32())

        if (packedSize >= buffer.length()) packedSize = 0

        if (entryName == "" && originalSize == 0L && offset == 0L && timestamp == 0L && packedSize == 0L) {
            if (parsedMime == EntryMimeType.VERSION) return PboVersionEntry.INFILE(
                pbo,
                buffer,
                flagPtr!!,
                readPboProperties(pbo)
            ).apply {
                properties.forEach { it.owner = this }
            }
            if (parsedMime == EntryMimeType.DUMMY) return PboDummyEntry.INFILE(pbo, buffer, flagPtr!!)
        }

        return PboDataEntry.INFILE(
            pbo,
            buffer,
            flagPtr!!,
            entryName,
            offset,
            timestamp,
            parsedMime,
            originalSize,
            packedSize
        )
    }

    private fun readPboProperties(pbo: PboFile): MutableList<PboProperty> {
        val properties = mutableListOf<PboProperty>()

        var propertyName: String
        while (buffer.readAsciiZ().also { propertyName = it }.isNotEmpty()) {
            val propertyValue: String = buffer.readAsciiZ()
            if (propertyName.lowercase() == "prefix") PboFile.normalizePath(propertyValue)?.let {
                pbo.pboPrefix = it
            }
            properties.add(PboProperty(propertyName, propertyValue))
        }

        return properties
    }

    override fun close() {
        buffer.close()
    }
}

