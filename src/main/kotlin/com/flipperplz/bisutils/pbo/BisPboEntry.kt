package com.flipperplz.bisutils.pbo

import com.flipperplz.bisutils.pbo.misc.BisPboProperty
import com.flipperplz.bisutils.pbo.misc.EntryMimeType
import com.flipperplz.bisutils.pbo.misc.StagedPboDataEntry
import com.flipperplz.bisutils.pbo.misc.StagedPboEntry
import com.flipperplz.bisutils.utils.readBytes
import java.io.RandomAccessFile
import java.nio.ByteBuffer

sealed interface BisPboEntry {
    var mimeType: EntryMimeType
    var fileName: String
    var timeStamp: Int
    var offset: Int
    var originalSize: Int
    var size: Int

    fun calculateMetadataLength(): Long = 21L + fileName.length
}

abstract class BisPboDummyEntry : BisPboEntry {
    override var fileName: String = ""
    override var mimeType: EntryMimeType = EntryMimeType.DUMMY
    override var size: Int = 0
    override var offset: Int = 0
    override var originalSize: Int = 0
    override var timeStamp: Int = 0

    override fun calculateMetadataLength(): Long = 21

    object CACHED : BisPboDummyEntry()
    class INFILE(
        override val stageBuffer: RandomAccessFile,
        offset: Long
    ) : BisPboDummyEntry(), StagedPboEntry {
        override var metadataOffset: Long = offset
    }
}

abstract class BisPboVersionEntry(
    val properties: List<BisPboProperty>
) : BisPboEntry {
    override var fileName: String = ""
    override var mimeType: EntryMimeType = EntryMimeType.DUMMY
    override var size: Int = 0
    override var offset: Int = 0
    override var originalSize: Int = 0
    override var timeStamp: Int = 0

    override fun calculateMetadataLength(): Long = 21 + properties.sumOf { it.calculateLength() ?: 0 } + 1

    class CACHED(properties: MutableList<BisPboProperty>): BisPboVersionEntry(properties) {
        companion object {
            fun withPrefix(prefix: String): CACHED = CACHED(mutableListOf(BisPboProperty("prefix", prefix.lowercase())))
        }
    }
    class INFILE(override val stageBuffer: RandomAccessFile, offset: Long, properties: List<BisPboProperty>) : BisPboVersionEntry(properties.toMutableList()), StagedPboEntry {
        override var metadataOffset: Long = offset
    }
}

abstract class BisPboDataEntry(
    override var fileName: String,
    override var offset: Int,
    override var timeStamp: Int,
    override var mimeType: EntryMimeType,
    override var originalSize: Int,
    override var size: Int,
) : BisPboEntry {
    internal abstract val entryData: ByteBuffer


    class CACHED(
        fileName: String,
        offset: Int,
        timeStamp: Int,
        mimeType: EntryMimeType,
        originalSize: Int,
        size: Int,
        override val entryData: ByteBuffer
    ) : BisPboDataEntry(
        fileName,
        offset,
        timeStamp,
        mimeType,
        originalSize,
        size
    )

    class INFILE(
        override val stageBuffer: RandomAccessFile,
        override var metadataOffset: Long,
        fileName: String,
        offset: Int,
        timeStamp: Int,
        mimeType: EntryMimeType,
        originalSize: Int,
        size: Int,
    ) : BisPboDataEntry(
        fileName,
        offset,
        timeStamp,
        mimeType,
        originalSize,
        size
    ), StagedPboDataEntry {
        override val entryData: ByteBuffer
            get() {
                val start = stageBuffer.filePointer
                stageBuffer.seek(dataOffset ?: throw Exception())

                return (if((stageBuffer.length() - stageBuffer.filePointer) > size)
                    ByteBuffer.allocate(0) else
                    ByteBuffer.wrap(stageBuffer.readBytes(size))).also {
                    stageBuffer.seek(start)
                }
            }

        override var dataOffset: Long? = null
    }
}