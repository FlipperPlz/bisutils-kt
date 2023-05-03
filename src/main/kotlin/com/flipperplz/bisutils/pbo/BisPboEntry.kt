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

    abstract val properties: List<BisPboProperty>
    override var fileName: String = ""
    override var mimeType: EntryMimeType = EntryMimeType.DUMMY
    override var size: Int = 0
    override var offset: Int = 0
    override var originalSize: Int = 0
    override var timeStamp: Int = 0

    override fun calculateMetadataLength(): Long = 21 + properties.sumOf { it.calculateLength() } + 1

    class CACHED internal constructor(
        override val initialOwner: BisPboFile?,
        override val properties: List<BisPboProperty>): BisPboVersionEntry() {
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

        override val properties: List<BisPboProperty> by Delegates.observable(properties) {
                _, _, _ -> onEditsMade()
        }
    }
}

abstract class BisPboDataEntry(
    fileName: String,
    override var offset: Int,
    override var timeStamp: Int,
    override var mimeType: EntryMimeType,
    override var originalSize: Int,
    override var size: Int,
) : BisPboEntry {
    internal abstract val entryData: ByteBuffer


    var path: String = BisPboFile.normalizePath(fileName)
        private set
    var segmentedPath: List<String> = path.split("\\")
        private set
    override var fileName: String by Delegates.observable(fileName) { _, old, new ->
        nameChanged(old, new)
    }

    protected open fun nameChanged(oldName: String, newName: String) {
        if(oldName == newName) return
        path = BisPboFile.normalizePath(newName)
    }

    class CACHED internal constructor(
        override val initialOwner: BisPboFile?,
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

    class INFILE internal constructor(
        override val initialOwner: BisPboFile,
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
        override var synced: Boolean = true
        override var dataOffset: Long? = null
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

        override fun nameChanged(oldName: String, newName: String) =  super.nameChanged(oldName, newName).also {
            onEditsMade()
        }
    }
}