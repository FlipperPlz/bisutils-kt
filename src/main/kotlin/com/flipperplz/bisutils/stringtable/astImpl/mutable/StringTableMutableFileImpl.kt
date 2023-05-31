package com.flipperplz.bisutils.stringtable.astImpl.mutable

import com.flipperplz.bisutils.stringtable.ast.StringTableLocalization
import com.flipperplz.bisutils.stringtable.ast.mutable.StringTableMutableFile
import com.flipperplz.bisutils.stringtable.util.StringTableFormat

data class StringTableMutableFileImpl(
    override val localizations: MutableList<StringTableLocalization> = mutableListOf()
) : StringTableMutableFile