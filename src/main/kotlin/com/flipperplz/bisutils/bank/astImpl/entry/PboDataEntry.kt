package com.flipperplz.bisutils.bank.astImpl.entry

import com.flipperplz.bisutils.bank.ast.entry.IPboDataEntry
import com.flipperplz.bisutils.bank.astImpl.PboEntry
import com.flipperplz.bisutils.bank.options.PboDebinarizationOptions
import com.flipperplz.bisutils.bank.utils.EntryMimeType
import com.flipperplz.bisutils.family.IFamilyNode
import java.nio.ByteBuffer
import java.nio.charset.Charset

class PboDataEntry(node: IFamilyNode?, parent: IFamilyNode?, override val entryData: ByteBuffer) : PboEntry(node, parent, "", EntryMimeType.VERSION, 0, 0, 0, 0), IPboDataEntry {
    override fun read(buffer: ByteBuffer, charset: Charset, options: PboDebinarizationOptions): Boolean =
        throw Exception("Not Supported")
}