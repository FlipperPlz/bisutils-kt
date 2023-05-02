package com.flipperplz.bisutils.pbo

import com.flipperplz.bisutils.pbo.misc.BisPboProperty
import com.flipperplz.bisutils.pbo.misc.EntryMimeType
import com.flipperplz.bisutils.pbo.misc.StagedPboDataEntry
import com.flipperplz.bisutils.pbo.misc.StagedPboEntry
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

open class BisPboDummyEntry : BisPboEntry {
    override var fileName: String = ""
    override var mimeType: EntryMimeType = EntryMimeType.DUMMY
    override var size: Int = 0
    override var offset: Int = 0
    override var originalSize: Int = 0
    override var timeStamp: Int = 0

    override fun calculateMetadataLength(): Long = 21

    class INFILE(
        override val stageBuffer: RandomAccessFile,
        offset: Long
    ) : BisPboDummyEntry(), StagedPboEntry {
        override var metadataOffset: Long = offset
    }
}

open class BisPboVersionEntry(
    val properties: MutableList<BisPboProperty>
) : BisPboEntry {
    override var fileName: String = ""
    override var mimeType: EntryMimeType = EntryMimeType.DUMMY
    override var size: Int = 0
    override var offset: Int = 0
    override var originalSize: Int = 0
    override var timeStamp: Int = 0

    override fun calculateMetadataLength(): Long = 21 + properties.sumOf { it.calculateLength() ?: 0 } + 1

    class INFILE(
        override val stageBuffer: RandomAccessFile,
        offset: Long,
        properties: List<BisPboProperty>
    ) : BisPboVersionEntry(properties.toMutableList()), StagedPboEntry {
        override var metadataOffset: Long = offset
    }
}

open class BisPboDataEntry(
    override var fileName: String,
    override var offset: Int,
    override var timeStamp: Int,
    override var mimeType: EntryMimeType,
    override var originalSize: Int,
    override var size: Int
) : BisPboEntry {
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
        override var dataOffset: Long? = null
    }

    class CACHED(
        fileName: String,
        offset: Int,
        timeStamp: Int,
        mimeType: EntryMimeType,
        originalSize: Int,
        dataBuffer: ByteBuffer
    ) : BisPboDataEntry(
        fileName,
        offset,
        timeStamp,
        mimeType,
        originalSize,
        dataBuffer.limit()
    )
}