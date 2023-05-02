package com.flipperplz.bisutils

import com.flipperplz.bisutils.pbo.BisPboFile
import com.flipperplz.bisutils.pbo.io.BisPboReader
import com.flipperplz.bisutils.utils.BisRandomAccessFile
import java.nio.channels.FileChannel

object BisPboManager {
    private val managedPbos = mutableMapOf<BisPboFile, BisPboReader>()

    fun managePbo(file: BisPboFile, reader: BisPboReader) {
        managedPbos[file] = reader
    }

    fun releasePbo(file: BisPboFile) = getPboChannel(file)?.let {
        it.lock()?.close()
        it.close()
    }

    fun getBuffer(file: BisPboFile): BisRandomAccessFile? {
        if(!isManaged(file)) return null
        return managedPbos[file]?.buffer
    }

    fun isManaged(file: BisPboFile) = managedPbos.containsKey(file)
    fun unlockPBO(file: BisPboFile) = getBuffer(file)?.channel?.lock()?.release()

    fun lockPBO(file: BisPboFile) = getBuffer(file)?.channel?.lock()?.release()

    fun getPboChannel(file: BisPboFile): FileChannel? = getBuffer(file)?.channel


}