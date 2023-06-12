package com.flipperplz.bisutils.bank.astImpl.mutable

import com.flipperplz.bisutils.bank.ast.mutable.IMutablePboVFSEntry
import com.flipperplz.bisutils.bank.astImpl.PboFile

open class MutablePboFile(
        override var defaultPrefix: String = "",
        override val familyChildren: MutableList<IMutablePboVFSEntry> = mutableListOf(),
        override var signature: ByteArray = byteArrayOf()
) : PboFile(defaultPrefix, familyChildren, signature) {


}