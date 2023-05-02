package com.flipperplz.bisutils.pbo.io

import com.flipperplz.bisutils.pbo.BisPboDataEntry
import com.flipperplz.bisutils.pbo.BisPboDummyEntry
import com.flipperplz.bisutils.pbo.BisPboEntry
import com.flipperplz.bisutils.pbo.BisPboVersionEntry
import com.flipperplz.bisutils.pbo.misc.BisPboProperty
import com.flipperplz.bisutils.pbo.misc.EntryMimeType
import com.flipperplz.bisutils.pbo.misc.StagedPboDataEntry
import com.flipperplz.bisutils.utils.BisRandomAccessFile
import com.flipperplz.bisutils.utils.readAsciiZ
import com.flipperplz.bisutils.utils.readInt32
import java.io.File
import kotlin.math.abs

class BisPboReader(internal val buffer: BisRandomAccessFile) : AutoCloseable {
    constructor(file: File) : this(BisRandomAccessFile(file, "r"))

    val pos: Long
        get() = buffer.filePointer
    private var flagPtr: Long? = null;

    fun initializeOffsets(entries: List<BisPboEntry>) {
        val headerPtr = entries.sumOf { it.calculateMetadataLength() }
        buffer.seek(headerPtr)

        for (entry in entries) {
            if(entry is StagedPboDataEntry) entry.dataOffset = buffer.filePointer
            if(entry is BisPboDataEntry) buffer.skipBytes(entry.size)
        }
    }

    fun readMetaBlock(): MutableList<BisPboEntry> {
        val entryList: MutableList<BisPboEntry> = mutableListOf()
        var currentEntry: BisPboEntry? = null

        do {
            val read = readEntryMeta()
            if(read == null) {
                if(currentEntry == null || flagPtr == null) throw Exception("Failed to read entry")
                if(currentEntry !is BisPboDummyEntry)  throw Exception("Failed to recover")
                currentEntry = BisPboDataEntry.INFILE(buffer, flagPtr!!, "", 0, 0, EntryMimeType.DUMMY,  0, 0)
                entryList.add(currentEntry)
                buffer.seek(flagPtr!!)
                break
            }

            currentEntry = read
            currentEntry.let { entryList.add(it) }
        } while (currentEntry !is BisPboDummyEntry)

        return entryList
    }

    fun readEntryMeta(): BisPboEntry? {
        flagPtr = pos

        val entryName = buffer.readAsciiZ()
        val parsedMime = EntryMimeType.fromMime(buffer.readInt32()) ?: return null
        val originalSize = abs(buffer.readInt32())
        val offset = abs(buffer.readInt32())
        val timestamp = buffer.readInt32()
        val packedSize = abs(buffer.readInt32())

        if(entryName == "" && originalSize == 0 && offset == 0 && timestamp == 0 && packedSize == 0) {
            if(parsedMime == EntryMimeType.VERSION) return BisPboVersionEntry.INFILE(buffer, flagPtr!!, readPboProperties())
            if(parsedMime == EntryMimeType.DUMMY) return BisPboDummyEntry.INFILE(buffer, flagPtr!!)
        }

        return BisPboDataEntry.INFILE(buffer, flagPtr!!, entryName, offset, timestamp, parsedMime, originalSize, packedSize)
    }

    fun readPboProperties(): List<BisPboProperty> {
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

