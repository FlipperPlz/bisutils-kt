package com.flipperplz.bisutils.bank.astImpl.mutable

import com.flipperplz.bisutils.bank.ast.IPboDirectory
import com.flipperplz.bisutils.bank.ast.IPboEntry
import com.flipperplz.bisutils.bank.ast.mutable.IMutablePboDirectory
import com.flipperplz.bisutils.bank.ast.mutable.IMutablePboEntry
import com.flipperplz.bisutils.bank.ast.mutable.IMutablePboFile
import com.flipperplz.bisutils.bank.ast.mutable.IMutablePboVFSEntry
import com.flipperplz.bisutils.bank.astImpl.PboFile

open class MutablePboFile(
    override var defaultPrefix: String = "",
    override val children: MutableList<IMutablePboVFSEntry> = mutableListOf(),
    override var signature: ByteArray = byteArrayOf()
) : PboFile(defaultPrefix, children, signature) {


}