package com.flipperplz.bisutils.bank.astImpl.entry

import com.flipperplz.bisutils.bank.ast.entry.IPboDataEntry
import com.flipperplz.bisutils.bank.astImpl.PboEntry
import com.flipperplz.bisutils.bank.utils.EntryMimeType
import com.flipperplz.bisutils.family.interfaces.IFamilyNode
import java.nio.ByteBuffer

class PboDataEntry(node: IFamilyNode?, parent: IFamilyNode?, override val entryData: ByteBuffer) : PboEntry(node, parent, "", EntryMimeType.VERSION, 0, 0, 0, 0), IPboDataEntry