package com.flipperplz.bisutils.bank.ast

import com.flipperplz.bisutils.family.interfaces.FamilyMember
import com.flipperplz.bisutils.family.interfaces.FamilyParent
import com.flipperplz.bisutils.binarization.BisStrictBinarizable

interface PboFile :  BisStrictBinarizable, FamilyParent {
    val defaultPrefix: String
    val entries: List<PboEntry>
    var signature: ByteArray //should always be 20 bytes **IIRC**

    override val children: List<FamilyMember>?
        get() = entries
    override val binaryLength: Long
        get() = TODO()//TODO: calculate length

    override fun isValid(): Boolean {
        if(!entries.all { it.isValid() }) return false
        TODO()
    }

}