package com.flipperplz.bisutils.bank.ast

import com.flipperplz.bisutils.family.interfaces.IFamilyMember
import com.flipperplz.bisutils.family.interfaces.IFamilyParent
import com.flipperplz.bisutils.binarization.interfaces.IStrictBinarizable

interface IPboFile :  IStrictBinarizable, IFamilyParent {
    val defaultPrefix: String
    val entries: List<IPboEntry>
    val signature: ByteArray //should always be 20 bytes **IIRC**

    override val children: List<IFamilyMember>?
        get() = entries
    override val binaryLength: Long
        get() = TODO()//TODO: calculate length

    override fun isValid(): Boolean {
        if(!entries.all { it.isValid() }) return false
        TODO()
    }

}