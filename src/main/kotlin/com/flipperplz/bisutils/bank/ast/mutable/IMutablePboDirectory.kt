package com.flipperplz.bisutils.bank.ast.mutable

import com.flipperplz.bisutils.bank.ast.IPboDirectory
import com.flipperplz.bisutils.bank.options.PboEntryDebinarizationOptions
import java.nio.ByteBuffer

interface IMutablePboDirectory : IPboDirectory, IMutablePboVFSEntry {
    override var parent: IMutablePboDirectory?
    override var node: IMutablePboFile?
    override val children: MutableList<IMutablePboVFSEntry>?
    override var entryName: String

    override var directories: List<IMutablePboDirectory>
        get() = children?.filterIsInstance<IMutablePboDirectory>() ?: emptyList()
        set(value) {children?.removeIf { it is IPboDirectory }; children?.addAll(value)}

    override var entries: List<IMutablePboEntry>
        get() = children?.filterIsInstance<IMutablePboEntry>() ?: emptyList()
        set(value) {children?.removeIf { it is IMutablePboEntry }; children?.addAll(value)}

    override val absolutePath: String
        get() = super<IPboDirectory>.absolutePath

    override val path: String
        get() = super<IPboDirectory>.path

    override fun read(buffer: ByteBuffer, options: PboEntryDebinarizationOptions): Boolean =
        throw Exception("Directories CANNOT be read directly, they must be formed by the pbo reader.")
}