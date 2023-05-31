package com.flipperplz.bisutils.stringtable.astImpl.mutable

import com.flipperplz.bisutils.stringtable.ast.StringTableFile
import com.flipperplz.bisutils.stringtable.ast.StringTableLanguage
import com.flipperplz.bisutils.stringtable.ast.mutable.StringTableMutableLocalization

data class StringTableMutableLocalizationImpl(
    override var name: String,
    override val definitions: MutableMap<StringTableLanguage, String> = mutableMapOf(),
    override val parsedFrom: StringTableFile? = null
) : StringTableMutableLocalization