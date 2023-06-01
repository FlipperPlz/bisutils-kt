@file:Suppress("DELEGATED_MEMBER_HIDES_SUPERTYPE_OVERRIDE")

package com.flipperplz.bisutils.bank.astImpl.mutable

import com.flipperplz.bisutils.bank.ast.mutable.MutablePboFile
import com.flipperplz.bisutils.bank.ast.PboEntry
import com.flipperplz.bisutils.bank.astImpl.PboFileImpl
import com.flipperplz.bisutils.family.interfaces.FamilyNode

class MutablePboFileImpl(
    override var defaultPrefix: String,
    override var entries: MutableList<PboEntry>,
    override var parent: FamilyNode?
):  PboFileImpl(defaultPrefix, entries, parent),
    MutablePboFile {
    override lateinit var signature: ByteArray
}