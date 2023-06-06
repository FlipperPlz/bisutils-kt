package com.flipperplz.bisutils.bank.astImpl.mutable

import com.flipperplz.bisutils.bank.ast.mutable.IMutablePboVFSEntry
import com.flipperplz.bisutils.bank.astImpl.PboFile

open class MutablePboFile(
    override var defaultPrefix: String = "",
    override val children: MutableList<IMutablePboVFSEntry> = mutableListOf(),
    override var signature: ByteArray
) : PboFile(defaultPrefix, children, signature)