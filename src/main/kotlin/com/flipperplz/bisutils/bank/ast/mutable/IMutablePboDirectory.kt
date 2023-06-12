package com.flipperplz.bisutils.bank.ast.mutable

import com.flipperplz.bisutils.bank.ast.IPboDirectory
import com.flipperplz.bisutils.bank.ast.IPboFile
import com.flipperplz.bisutils.bank.astImpl.mutable.MutablePboDirectory
import com.flipperplz.bisutils.bank.options.PboOptions
import java.nio.ByteBuffer

interface IMutablePboDirectory : IPboDirectory, IMutablePboVFSEntry, Cloneable {
    override var familyParent: IPboDirectory?
    override var familyNode: IPboFile?
    override val familyChildren: MutableList<IMutablePboVFSEntry>
    override var entryName: String

    override var directories: List<IMutablePboDirectory>
        get() = familyChildren.filterIsInstance<IMutablePboDirectory>()
        set(value) {familyChildren.removeIf { it is IPboDirectory }; familyChildren.addAll(value)}

    override var entries: List<IMutablePboEntry>
        get() = familyChildren.filterIsInstance<IMutablePboEntry>()
        set(value) {familyChildren.removeIf { it is IMutablePboEntry }; familyChildren.addAll(value)}

    override val absolutePath: String
        get() = super<IPboDirectory>.absolutePath

    override val path: String
        get() = super<IPboDirectory>.path

    override fun read(buffer: ByteBuffer, options: PboOptions): Boolean =
        throw Exception("Directories CANNOT be read directly, they must be formed by the pbo reader.")

    override fun clone(): IMutablePboDirectory = super<Cloneable>.clone() as IMutablePboDirectory

    fun createDirectory(name: String): IMutablePboDirectory = with(name.split("\\", limit = 2)) {
        getDirectory(this[0])?.let { return it.createDirectory(this[1]) }
        return MutablePboDirectory(familyNode, this@IMutablePboDirectory, this[0], mutableListOf()).also {
            familyChildren.add(it.createDirectory(this[1]))
        }
    }

    fun getDirectory(name: String): IMutablePboDirectory? = directories.firstOrNull { it.entryName == name }

}