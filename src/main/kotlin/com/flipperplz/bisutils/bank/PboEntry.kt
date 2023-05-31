package com.flipperplz.bisutils.bank

import com.flipperplz.bisutils.bank.utils.PboProperty
import com.flipperplz.bisutils.bank.utils.EntryMimeType
import com.flipperplz.bisutils.bank.utils.StagedPboDataEntry
import com.flipperplz.bisutils.bank.utils.StagedPboEntry
import com.flipperplz.bisutils.utils.readBytes
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import kotlin.properties.Delegates

sealed interface PboEntry {
    val initialOwner: PboFile?

    var mimeType: EntryMimeType
    var fileName: String
    var timeStamp: Long
    var offset: Long
    var originalSize: Long
    var size: Long

    fun calculateMetadataLength(): Long = 21L + fileName.length
}

abstract class PboDummyEntry : PboEntry {
    override var fileName: String = ""
    override var mimeType: EntryMimeType = EntryMimeType.DUMMY
    override var size: Long = 0
    override var offset: Long = 0
    override var originalSize: Long = 0
    override var timeStamp: Long = 0
    override val initialOwner: PboFile? = null
    override fun calculateMetadataLength(): Long = 21

    object CACHED : PboDummyEntry()
    class INFILE internal constructor(
        override val initialOwner: PboFile,
        override val stageBuffer: RandomAccessFile,
        offset: Long
    ) : PboDummyEntry(), StagedPboEntry {
        override var synced: Boolean = true
        override var metadataOffset: Long = offset
    }
}

abstract class PboVersionEntry : PboEntry {

    abstract var properties: List<PboProperty>
    override var fileName: String = ""
    override var mimeType: EntryMimeType = EntryMimeType.DUMMY
    override var size: Long = 0
    override var offset: Long = 0
    override var originalSize: Long = 0
    override var timeStamp: Long = 0

    override fun calculateMetadataLength(): Long = 21 + properties.sumOf { it.calculateLength() } + 1

    class CACHED internal constructor(
        override val initialOwner: PboFile?,
        override var properties: List<PboProperty>
    ) : PboVersionEntry() {
        companion object {
            fun withPrefix(prefix: String, owner: PboFile? = null): CACHED =
                CACHED(owner, mutableListOf(PboProperty("prefix", prefix.lowercase())))
        }
    }

    class INFILE internal constructor(
        override val initialOwner: PboFile,
        override val stageBuffer: RandomAccessFile,
        offset: Long,
        properties: List<PboProperty>
    ) : PboVersionEntry(), StagedPboEntry {
        override var synced: Boolean = true
        override var metadataOffset: Long = offset

        override var properties: List<PboProperty> by Delegates.observable(properties) { _, _, _ ->
            onEditsMade()
        }
    }
}

abstract class PboDataEntry(
    fileName: String,
    override var offset: Long,
    override var timeStamp: Long,
    override var mimeType: EntryMimeType,
    override var originalSize: Long,
    override var size: Long,
) : PboEntry {
    internal abstract val entryData: ByteBuffer


    var path: String = PboFile.normalizePath(fileName) ?: "BUx0001: \"$fileName\""
        private set
    var segmentedPath: List<String> = path.split("\\")
        private set
    override var fileName: String by Delegates.observable(fileName) { _, old, new ->
        nameChanged(old, new)
    }

    protected open fun nameChanged(oldName: String, newName: String) {
        if (oldName == newName) return
        path = PboFile.normalizePath(newName) ?: "BUx0001: \"$newName\""
    }

    class CACHED internal constructor(
        override val initialOwner: PboFile?,
        fileName: String,
        offset: Long,
        timeStamp: Long,
        mimeType: EntryMimeType,
        originalSize: Long,
        size: Long,
        override val entryData: ByteBuffer
    ) : PboDataEntry(
        fileName,
        offset,
        timeStamp,
        mimeType,
        originalSize,
        size
    )

    class INFILE internal constructor(
        override val initialOwner: PboFile,
        override val stageBuffer: RandomAccessFile,
        override var metadataOffset: Long,
        fileName: String,
        offset: Long,
        timeStamp: Long,
        mimeType: EntryMimeType,
        originalSize: Long,
        size: Long,
    ) : PboDataEntry(
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

                return (if ((stageBuffer.length() - stageBuffer.filePointer) > size)
                    ByteBuffer.allocate(0) else
                    ByteBuffer.wrap(stageBuffer.readBytes(size.toInt()))).also {
                    stageBuffer.seek(start)
                }
            }

        override fun nameChanged(oldName: String, newName: String) = super.nameChanged(oldName, newName).also {
            onEditsMade()
        }
    }
}