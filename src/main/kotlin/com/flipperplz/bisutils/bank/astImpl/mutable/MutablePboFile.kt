package com.flipperplz.bisutils.bank.astImpl.mutable

import com.flipperplz.bisutils.bank.ast.mutable.IMutablePboFile
import com.flipperplz.bisutils.bank.ast.IPboEntry
import com.flipperplz.bisutils.bank.astImpl.PboFile
import com.flipperplz.bisutils.family.interfaces.IFamilyNode
import com.flipperplz.bisutils.io.getBytes
import com.flipperplz.bisutils.utils.IFlushable
import java.io.Closeable
import java.nio.ByteBuffer
import java.nio.charset.Charset

class MutablePboFile(
    override var defaultPrefix: String,
    override var parent: IFamilyNode?,
    override var entries: MutableList<IPboEntry>,
    override var signature: ByteArray,
): PboFile(defaultPrefix, parent, entries, signature), IMutablePboFile {

    override fun read(buffer: ByteBuffer, charset: Charset): Boolean {
        flush()
        entries = mutableListOf() //TODO: Read PBO
        signature = buffer.getBytes(buffer.remaining())
        return true
    }

    override fun flush() {
        super.flush()
        entries.filterIsInstance<IFlushable>().forEach { it.flush() }
        entries.filterIsInstance<Closeable>().forEach { it.close() }
        signature = byteArrayOf() //TODO: Default Signature
    }
}