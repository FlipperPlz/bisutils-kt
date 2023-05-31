package com.flipperplz.bisutils.bank

import com.flipperplz.bisutils.BisPboManager
import com.flipperplz.bisutils.bank.io.BisPboReader
import com.flipperplz.bisutils.bank.utils.PboProperty
import com.flipperplz.bisutils.bank.utils.EntryMimeType
import com.flipperplz.bisutils.utils.BisRandomAccessFile
import com.flipperplz.bisutils.utils.decompress
import com.google.common.cache.CacheBuilder
import java.io.File
import java.io.FileOutputStream
import java.nio.ByteBuffer

class PboFile internal constructor(prefix: String) : AutoCloseable {
    internal val entries: MutableList<PboEntry> = mutableListOf()

    //TODO: UNUSED
    private val dataCache = CacheBuilder.newBuilder().build<PboDataEntry, ByteBuffer>()
    val pboEntries: List<PboEntry> = entries
    var pboPrefix: String = prefix
        get() = normalizePath(customPrefix?.propertyValue) ?: field
        internal set(value) {
            customPrefix?.let { it.owner.properties -= it; return }
            field = value
        }

    val customPrefix: PboProperty?
        get() = entries.filterIsInstance<PboVersionEntry>().flatMap { it.properties }
            .lastOrNull { it.propertyName == "prefix" }

    override fun close() {
        dataCache.cleanUp()
        BisPboManager.releasePbo(this)
    }

    inline fun extractPBO(folder: File, pathChooser: (File, PboDataEntry, Int) -> File) {
        val root = File(folder, pboPrefix)

        if (root.exists() && !root.deleteRecursively()) throw Exception("Failed to delete extraction root ${root.absolutePath}!")
        if (!root.mkdirs()) throw Exception("Failed to create extraction root ${root.absolutePath}!")

        for ((i, e) in pboEntries.filterIsInstance<PboDataEntry>().withIndex()) {
            val file = pathChooser(root, e, i)
            if (file.exists() && !file.delete()) throw Exception("Failed to delete entry ${file.absolutePath} to make room for entry with duplicate name!")
            if (!file.createNewFile()) throw Exception("Failed to create ${file.path}!")
            FileOutputStream(file).channel.use { it.write(retrieveEntryData(e, false)) }
        }
    }

    class PboPseudoDirectory(
        val name: String,
        val parent: PboPseudoDirectory?,
        val childrenFolders: MutableList<PboPseudoDirectory> = mutableListOf(),
        val childrenFiles: MutableList<PboDataEntry> = mutableListOf()
    ) {
        fun getOrCreateDirectory(entryPath: List<String>): PboPseudoDirectory {
            val directory = entryPath.firstOrNull() ?: return this
            val rest = entryPath.drop(1)
            childrenFolders.firstOrNull { it.name == directory }?.getOrCreateDirectory(rest)?.let { return it }
            val created = PboPseudoDirectory(directory, this)
            childrenFolders.add(created)
            return created.getOrCreateDirectory(rest)
        }
    }

    fun getAbsoluteEntryPath(entry: PboDataEntry): String =
        StringBuilder(pboPrefix).append('\\').append(normalizePath(entry.path)?.trimStart('\\')).toString()

    fun createEntryTree(): PboPseudoDirectory {
        val root = PboPseudoDirectory(pboPrefix, null)

        for (entry in entries.filterIsInstance<PboDataEntry>()) {
            val splitPath = entry.segmentedPath
            if (splitPath.size == 1) root.childrenFiles.add(entry)
            else root.getOrCreateDirectory(splitPath.dropLast(1)).also {
                it.childrenFiles.add(entry)
            }
        }

        return root
    }

    fun retrieveEntryData(entry: PboDataEntry, raw: Boolean): ByteBuffer {
        if (raw || entry.mimeType == EntryMimeType.DUMMY) return entry.entryData
        if (entry.mimeType == EntryMimeType.ENCRYPTED_DATA) throw Exception("EBO not supported.")
        if (entry.size != entry.originalSize && entry.mimeType == EntryMimeType.NORMAL_DATA) {
            return entry.entryData.decompress(entry.originalSize, true)
        }
        return entry.entryData
    }

    companion object {

        fun read(file: File, lightRead: Boolean = true, allowWrite: Boolean = false): PboFile {
            val reader = BisPboReader(BisRandomAccessFile(file, if (allowWrite) "rw" else "r"))

            return if (lightRead) reader.lightRead() else reader.read()
        }

        fun create(prefix: String): PboFile {
            val pbo = PboFile(prefix)
            pbo.entries.add(PboVersionEntry.CACHED.withPrefix(prefix))
            pbo.entries.add(PboDummyEntry.CACHED)
            return pbo
        }

        fun normalizePath(path: String?): String? =
            path?.lowercase()?.replace(Regex("[\\\\/]+"), "\\\\")?.trimEnd('\\')
    }
}
