package com.flipperplz.bisutils.stringtable.ast.mutable

import com.flipperplz.bisutils.stringtable.ast.StringTableFile
import com.flipperplz.bisutils.stringtable.ast.StringTableLanguage
import com.flipperplz.bisutils.stringtable.ast.StringTableLocalization
import com.flipperplz.bisutils.utils.IFlushable

interface StringTableMutableFile : StringTableFile, MutableList<StringTableLocalization>, IFlushable {
    override val localizations: MutableList<StringTableLocalization>
    override val languages: MutableList<StringTableLanguage>

    fun append(local: StringTableFile) = localizations.addAll(local.localizations)

    override fun flush() {
        localizations.clear()
        languages.removeIf { it !is StringTableLanguage.English && it !is StringTableLanguage.Original}
    }
}