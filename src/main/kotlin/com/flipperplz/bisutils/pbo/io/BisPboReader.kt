package com.flipperplz.bisutils.pbo.io

import com.flipperplz.bisutils.pbo.BisPboDataEntry
import com.flipperplz.bisutils.pbo.BisPboDummyEntry
import com.flipperplz.bisutils.pbo.BisPboEntry
import com.flipperplz.bisutils.pbo.BisPboVersionEntry
import com.flipperplz.bisutils.pbo.misc.BisPboProperty
import com.flipperplz.bisutils.pbo.misc.EntryMimeType
import com.flipperplz.bisutils.utils.BisRandomAccessFile
import com.flipperplz.bisutils.utils.readAsciiZ
import com.flipperplz.bisutils.utils.readInt32
import kotlin.math.abs

class BisPboReader(private val buffer: BisRandomAccessFile) {
    val pos: Long
        get() = buffer.filePointer

    fun readEntryMeta(): BisPboEntry? {
        val metaPtr = pos

        val entryName = buffer.readAsciiZ()
        val parsedMime = EntryMimeType.fromMime(buffer.readInt32()) ?: return null
        val originalSize = abs(buffer.readInt32())
        val offset = abs(buffer.readInt32())
        val timestamp = buffer.readInt32()
        val packedSize = abs(buffer.readInt32())

        if(entryName == "" && originalSize == 0 && offset == 0 && timestamp == 0 && packedSize == 0) {
            if(parsedMime == EntryMimeType.VERSION) return BisPboVersionEntry.STAGED(buffer, metaPtr, readPboProperties())
            if(parsedMime == EntryMimeType.DUMMY) return BisPboDummyEntry.STAGED(buffer, metaPtr)
        }

        return BisPboDataEntry.STAGED(buffer,metaPtr, entryName, offset, timestamp, parsedMime, originalSize, packedSize)
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
}

