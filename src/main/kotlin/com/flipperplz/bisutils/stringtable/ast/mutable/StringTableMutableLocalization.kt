package com.flipperplz.bisutils.stringtable.ast.mutable

import com.flipperplz.bisutils.stringtable.ast.StringTableLanguage
import com.flipperplz.bisutils.stringtable.ast.StringTableLocalization

interface StringTableMutableLocalization : StringTableLocalization {
    override var name: String
    override val definitions: MutableMap<StringTableLanguage, String>

}