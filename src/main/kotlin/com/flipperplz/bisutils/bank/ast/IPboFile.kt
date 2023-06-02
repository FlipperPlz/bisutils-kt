package com.flipperplz.bisutils.bank.ast

import com.flipperplz.bisutils.bank.options.PboBinarizationOptions
import com.flipperplz.bisutils.bank.utils.IPboBinaryObject
import com.flipperplz.bisutils.bank.utils.getProperty
import com.flipperplz.bisutils.family.IFamilyMember
import com.flipperplz.bisutils.family.IFamilyParent
import java.nio.charset.Charset

interface IPboFile : IPboBinaryObject, IFamilyParent {
    val prefix: String
        get() = entries.getProperty("prefix")?.value ?: defaultPrefix /*TODO: DIG THROUGH VERSION ENTRY*/
    val defaultPrefix: String
    val entries: List<IPboEntry>
    val signature: ByteArray //should always be 20 bytes **IIRC**

    override val children: List<IFamilyMember>?
        get() = entries

    override fun calculateBinaryLength(charset: Charset, options: PboBinarizationOptions?): Long {
        TODO()//TODO: calculate length
    }

    override fun isValid(): Boolean {
        if(!entries.all { it.isValid() }) return false
        TODO()
    }

}