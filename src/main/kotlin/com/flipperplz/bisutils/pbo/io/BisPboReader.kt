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
        val result = BisPboFile()
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
                entry.fileName,
                entry.offset,
                entry.timeStamp,
                entry.mimeType,
                entry.originalSize,
                ByteBuffer.wrap(reader.readBytes(entry.size))
            )
            reader.seek(startPtr)
        }

        return pbo
    }

    private fun initializeFile(file: BisPboFile): BisPboFile = file.apply {
        entries.addAll(readMetaBlock())
        flagPtr = null
        initializeOffsets(entries)
    }

    private fun initializeOffsets(entries: List<BisPboEntry>) {
        val headerPtr = entries.sumOf { it.calculateMetadataLength() }
        buffer.seek(headerPtr)

        for (entry in entries) {
            if(entry is BisPboDataEntry) {

                if(entry is StagedPboDataEntry) entry.dataOffset = buffer.filePointer
                if((buffer.length() - buffer.filePointer) > entry.size) continue
                buffer.skipBytes(entry.size)
            }
        }
    }

    private fun readMetaBlock(): MutableList<BisPboEntry> {
        val entryList: MutableList<BisPboEntry> = mutableListOf()
        var currentEntry: BisPboEntry? = null

        do {
            val read = readEntryMeta()
            if(read == null) {
                if(currentEntry == null || flagPtr == null) throw Exception("Failed to read entry")
                if(currentEntry !is BisPboDummyEntry)  throw Exception("Failed to recover")
                currentEntry = BisPboDataEntry.INFILE(
                    buffer,
                    flagPtr!!,
                    "",
                    0,
                    0,
                    EntryMimeType.DUMMY,
                    0,
                    0
                )
                entryList.add(currentEntry)
                buffer.seek(flagPtr!!)
                break
            }

            currentEntry = read
            currentEntry.let { entryList.add(it) }
        } while (currentEntry !is BisPboDummyEntry)

        return entryList
    }

    private fun readEntryMeta(): BisPboEntry? {
        flagPtr = pos

        val entryName = buffer.readAsciiZ()
        val parsedMime = EntryMimeType.fromMime(buffer.readInt32()) ?: return null
        val originalSize = abs(buffer.readInt32())
        val offset = abs(buffer.readInt32())
        val timestamp = buffer.readInt32()
        var packedSize = abs(buffer.readInt32())

        if(packedSize >= buffer.length()) packedSize = 0

        if(entryName == "" && originalSize == 0 && offset == 0 && timestamp == 0 && packedSize == 0) {
            if(parsedMime == EntryMimeType.VERSION) return BisPboVersionEntry.INFILE(buffer, flagPtr!!, readPboProperties())
            if(parsedMime == EntryMimeType.DUMMY) return BisPboDummyEntry.INFILE(buffer, flagPtr!!)
        }

        return BisPboDataEntry.INFILE(buffer, flagPtr!!, entryName, offset, timestamp, parsedMime, originalSize, packedSize)
    }

    private fun readPboProperties(): List<BisPboProperty> {
        val properties = mutableListOf<BisPboProperty>()

        var propertyName: String
        while (buffer.readAsciiZ().also { propertyName = it }.isNotEmpty()) {
            val propertyValue: String = buffer.readAsciiZ()
            properties.add(BisPboProperty(propertyName, propertyValue))
        }

        return properties
    }

    override fun close() {
        buffer.close()
    }
}

