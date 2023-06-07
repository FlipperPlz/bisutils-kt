package com.flipperplz.bisutils.bank.ast.misc.mutable

import com.flipperplz.bisutils.bank.ast.entry.mutable.IMutablePboVersionEntry
import com.flipperplz.bisutils.bank.ast.misc.IPboProperty
import com.flipperplz.bisutils.bank.ast.mutable.IMutablePboFile
import com.flipperplz.bisutils.bank.options.PboEntryDebinarizationOptions
import com.flipperplz.bisutils.binarization.options.DEFAULT_BIS_CHARSET
import com.flipperplz.bisutils.io.getAsciiZ
import java.nio.ByteBuffer

interface IMutablePboProperty : IPboProperty, Cloneable {
    override var node: IMutablePboFile?
    override var parent: IMutablePboVersionEntry?
    override var name: String
    override var value: String

    override fun read(buffer: ByteBuffer, options: PboEntryDebinarizationOptions): Boolean {
        name = buffer.getAsciiZ(options.charset ?: DEFAULT_BIS_CHARSET)
        if(name == "") return false
        value = buffer.getAsciiZ(options.charset ?: DEFAULT_BIS_CHARSET)
        return true
    }

    override fun clone(): IMutablePboProperty = super<Cloneable>.clone() as IMutablePboProperty
}