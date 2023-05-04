package com.flipperplz.bisutils.pbo

import com.flipperplz.bisutils.pbo.misc.BisPboProperty
import com.flipperplz.bisutils.pbo.misc.EntryMimeType
import com.flipperplz.bisutils.pbo.misc.StagedPboDataEntry
import com.flipperplz.bisutils.pbo.misc.StagedPboEntry
import com.flipperplz.bisutils.utils.readBytes
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

sealed interface BisPboEntry {
    val initialOwner: BisPboFile?

    var mimeType: EntryMimeType
    var fileName: String
    var timeStamp: Long
    var offset: Long
    var originalSize: Long
    var size: Long

    fun calculateMetadataLength(): Long = 21L + fileName.length
}

abstract class BisPboDummyEntry : BisPboEntry {
    override var fileName: String = ""
    override var mimeType: EntryMimeType = EntryMimeType.DUMMY
    override var size: Long = 0
    override var offset: Long = 0
    override var originalSize: Long = 0
    override var timeStamp: Long = 0
    override val initialOwner: BisPboFile? = null
    override fun calculateMetadataLength(): Long = 21

    object CACHED : BisPboDummyEntry()
    class INFILE internal constructor(
        override val initialOwner: BisPboFile,
        override val stageBuffer: RandomAccessFile,
        offset: Long
    ) : BisPboDummyEntry(), StagedPboEntry {
        override var synced: Boolean = true
        override var metadataOffset: Long = offset
    }
}

abstract class BisPboVersionEntry(): BisPboEntry {

    abstract var properties: List<BisPboProperty>
    override var fileName: String = ""
    override var mimeType: EntryMimeType = EntryMimeType.DUMMY
    override var size: Long = 0
    override var offset: Long = 0
    override var originalSize: Long = 0
    override var timeStamp: Long = 0

    override fun calculateMetadataLength(): Long = 21 + properties.sumOf { it.calculateLength() } + 1

    class CACHED internal constructor(
        override val initialOwner: BisPboFile?,
        override var properties: List<BisPboProperty>): BisPboVersionEntry() {
        companion object {
            fun withPrefix(prefix: String, owner: BisPboFile? = null): CACHED =
                CACHED(owner, mutableListOf(BisPboProperty("prefix", prefix.lowercase())))
        }
    }

    class INFILE internal constructor(
        override val initialOwner: BisPboFile,
        override val stageBuffer: RandomAccessFile,
        offset: Long,
        properties: List<BisPboProperty>
    ) : BisPboVersionEntry(), StagedPboEntry {
        override var synced: Boolean = true
        override var metadataOffset: Long = offset

        override var properties: List<BisPboProperty> by Delegates.observable(properties) {
                _, _, _ -> onEditsMade()
        }
    }
}

abstract class BisPboDataEntry(
    fileName: String,
    override var offset: Long,
    override var timeStamp: Long,
    override var mimeType: EntryMimeType,
    override var originalSize: Long,
    override var size: Long,
) : BisPboEntry {
    internal abstract val entryData: ByteBuffer


    var path: String = BisPboFile.normalizePath(fileName) ?: "BUx0001: \"$fileName\""
        private set
    var segmentedPath: List<String> = path.split("\\")
        private set
    override var fileName: String by Delegates.observable(fileName) { _, old, new ->
        nameChanged(old, new)
    }

    protected open fun nameChanged(oldName: String, newName: String) {
        if(oldName == newName) return
        path = BisPboFile.normalizePath(newName) ?: "BUx0001: \"$newName\""
    }

    class CACHED internal constructor(
        override val initialOwner: BisPboFile?,
        fileName: String,
        offset: Long,
        timeStamp: Long,
        mimeType: EntryMimeType,
        originalSize: Long,
        size: Long,
        override val entryData: ByteBuffer
    ) : BisPboDataEntry(
        fileName,
        offset,
        timeStamp,
        mimeType,
        originalSize,
        size
    )

    class INFILE internal constructor(
        override val initialOwner: BisPboFile,
        override val stageBuffer: RandomAccessFile,
        override var metadataOffset: Long,
        fileName: String,
        offset: Long,
        timeStamp: Long,
        mimeType: EntryMimeType,
        originalSize: Long,
        size: Long,
    ) : BisPboDataEntry(
        fileName,
        offset,
        timeStamp,
        mimeType,
        originalSize,
        size
    ), StagedPboDataEntry {
        override var synced: Boolean = true
        override var dataOffset: Long? = null
        override val entryData: ByteBuffer
            get() {
                val start = stageBuffer.filePointer
                stageBuffer.seek(dataOffset ?: throw Exception())

                return (if((stageBuffer.length() - stageBuffer.filePointer) > size)
                    ByteBuffer.allocate(0) else
                    ByteBuffer.wrap(stageBuffer.readBytes(size.toInt()))).also {
                    stageBuffer.seek(start)
                }
            }

        override fun nameChanged(oldName: String, newName: String) =  super.nameChanged(oldName, newName).also {
            onEditsMade()
        }
    }
}