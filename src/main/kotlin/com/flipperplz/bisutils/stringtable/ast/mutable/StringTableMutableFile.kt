package com.flipperplz.bisutils.stringtable.ast.mutable

import com.flipperplz.bisutils.stringtable.ast.StringTableFile
import com.flipperplz.bisutils.stringtable.ast.StringTableLanguage
import com.flipperplz.bisutils.stringtable.ast.StringTableLocalization

interface StringTableMutableFile : StringTableFile {
    override val localizations: MutableList<StringTableLocalization>
    override val languages: MutableList<StringTableLanguage>

    fun append(local: StringTableFile) = localizations.addAll(local.localizations)
}