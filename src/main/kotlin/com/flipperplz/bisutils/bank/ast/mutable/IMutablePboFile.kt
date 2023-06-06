package com.flipperplz.bisutils.bank.ast.mutable

import com.flipperplz.bisutils.bank.ast.IPboDirectory
import com.flipperplz.bisutils.bank.ast.IPboFile
import com.flipperplz.bisutils.bank.options.PboEntryDebinarizationOptions
import java.nio.ByteBuffer

interface IMutablePboFile : IPboFile, IMutablePboDirectory {
    override var parent: IMutablePboDirectory?
    override var node: IMutablePboFile?
    override var defaultPrefix: String
    override var signature: ByteArray
    override val children: MutableList<IMutablePboVFSEntry>?
    override val absolutePath: String get() = super<IMutablePboDirectory>.absolutePath
    override val path: String get() = super<IMutablePboDirectory>.path

    override var entryName: String
        get() = defaultPrefix
        set(value)  { defaultPrefix = value }//TODO Allow version property change if available

    override var directories: List<IMutablePboDirectory>
        get() = super.directories
        set(value) {super.directories = value}

    override var entries: List<IMutablePboEntry>
        get() = super.entries
        set(value) {super.entries = value}

    //TODO: READ
    override fun read(buffer: ByteBuffer, options: PboEntryDebinarizationOptions): Boolean {
        return super<IPboFile>.read(buffer, options)
    }
}