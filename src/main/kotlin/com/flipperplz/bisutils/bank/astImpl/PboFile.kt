package com.flipperplz.bisutils.bank.astImpl

import com.flipperplz.bisutils.bank.ast.IPboDirectory
import com.flipperplz.bisutils.bank.ast.IPboEntry
import com.flipperplz.bisutils.bank.ast.IPboFile
import com.flipperplz.bisutils.bank.ast.IPboVFSEntry
import com.flipperplz.bisutils.bank.options.PboOptions

open class PboFile(
    override val defaultPrefix: String = "",
    override val children: List<IPboVFSEntry> = emptyList(),
    override val signature: ByteArray
) : IPboFile {
    constructor(defaultPrefix: String, children: List<IPboVFSEntry>, options: PboOptions?) :
        this(defaultPrefix, children, IPboFile.calculateSignature(children, options))

    final override val entryName: String = super.entryName
    final override val prefix: String = super.prefix
    final override val node: PboFile? = null
    final override val absolutePath: String = super.absolutePath
    final override val path: String = super.path
    open override val parent: IPboDirectory? = null
    final override val entries: List<IPboEntry> = super.entries
    final override val directories: List<IPboDirectory> = super.directories
}