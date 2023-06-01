@file:Suppress("DELEGATED_MEMBER_HIDES_SUPERTYPE_OVERRIDE")

package com.flipperplz.bisutils.bank.astImpl.mutable

import com.flipperplz.bisutils.bank.ast.mutable.MutablePboFile
import com.flipperplz.bisutils.bank.ast.PboEntry
import com.flipperplz.bisutils.bank.ast.misc.mutable.MutablePboElement
import com.flipperplz.bisutils.bank.astImpl.PboFileImpl
import com.flipperplz.bisutils.utils.BisFamily

class MutablePboFileImpl(
    override var defaultPrefix: String,
    override val entries: MutableList<PboEntry>,
    override var parent: BisFamily? = null
):  PboFileImpl(defaultPrefix, entries, parent),
    MutablePboElement, MutablePboFile, MutableList<PboEntry> by entries {
    override lateinit var signature: ByteArray
}