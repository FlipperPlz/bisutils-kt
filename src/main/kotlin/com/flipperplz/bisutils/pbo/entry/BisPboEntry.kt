package com.flipperplz.bisutils.pbo.entry

import com.flipperplz.bisutils.pbo.misc.BisPboProperty
import com.flipperplz.bisutils.pbo.misc.EntryMimeType
import com.flipperplz.bisutils.pbo.misc.StagedPboDataEntry
import com.flipperplz.bisutils.pbo.misc.StagedPboEntry
import java.nio.ByteBuffer
import kotlin.properties.Delegates

sealed interface BisPboEntry {
    var mimeType: EntryMimeType
    var fileName: String
    var timeStamp: Int
    var offset: Int
    var originalSize: Int
    var size: Int

    fun calculateMetadataLength(): Long = 21L + fileName.length
}

sealed class BisPboDummyEntry : BisPboEntry {
    override var fileName: String = ""
    override var mimeType: EntryMimeType = EntryMimeType.DUMMY
    override var size: Int = 0
    override var offset: Int = 0
    override var originalSize: Int = 0
    override var timeStamp: Int = 0

    override fun calculateMetadataLength(): Long = 21

    class STAGED(offset: Long) : BisPboDummyEntry(), StagedPboEntry {
        override var metadataOffset: Long = offset
    }
}

sealed class BosPboVersionEntry(
    properties: List<BisPboProperty>
) : BisPboEntry {
    override var fileName: String = ""
    override var mimeType: EntryMimeType = EntryMimeType.DUMMY
    override var size: Int = 0
    override var offset: Int = 0
    override var originalSize: Int = 0
    override var timeStamp: Int = 0

    class STAGED(offset: Long, properties: MutableList<BisPboProperty>) : BosPboVersionEntry(properties), StagedPboEntry {
        override var metadataOffset: Long = offset
    }
}

sealed class BisPboDataEntry(
    override var fileName: String,
    override var offset: Int,
    override var timeStamp: Int,
    override var mimeType: EntryMimeType,
) : BisPboEntry {
    lateinit var buffer: ByteBuffer

    constructor(
        fileName: String,
        offset: Int,
        timeStamp: Int,
        mimeType: EntryMimeType,
        data: ByteBuffer,
        compress: Boolean
    ): this(fileName, offset, timeStamp, mimeType) {
        this.buffer = data
    }

    override var originalSize: Int
        get() = buffer.limit()
        set(value) { throw Exception() }

    override var size: Int
        get() = buffer.limit()
        set(value) {}

    class STAGED(
        override var metadataOffset: Long,
        fileName: String,
        offset: Int,
        timeStamp: Int,
        mimeType: EntryMimeType,
        override var size: Int,
        override var originalSize: Int
    ) : BisPboDataEntry(fileName, offset, timeStamp, mimeType), StagedPboDataEntry {
        override var dataOffset: Long? = null
    }
}