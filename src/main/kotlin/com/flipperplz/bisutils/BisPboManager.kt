package com.flipperplz.bisutils

import com.flipperplz.bisutils.bank.PboFile
import com.flipperplz.bisutils.bank.io.BisPboReader
import com.flipperplz.bisutils.utils.BisRandomAccessFile
import java.nio.channels.FileChannel

object BisPboManager {
    private val managedPbos = mutableMapOf<PboFile, BisPboReader>()

    fun managePbo(file: PboFile, reader: BisPboReader) {
        managedPbos[file] = reader
    }

    fun releasePbo(file: PboFile) = getPboChannel(file)?.let {
        it.lock()?.close()
        it.close()
    }

    fun getBuffer(file: PboFile): BisRandomAccessFile? {
        if (!isManaged(file)) return null
        return managedPbos[file]?.buffer
    }

    fun isManaged(file: PboFile) = managedPbos.containsKey(file)
    fun unlockPBO(file: PboFile) = getBuffer(file)?.channel?.lock()?.release()

    fun lockPBO(file: PboFile) = getBuffer(file)?.channel?.lock()?.release()

    fun getPboChannel(file: PboFile): FileChannel? = getBuffer(file)?.channel


}