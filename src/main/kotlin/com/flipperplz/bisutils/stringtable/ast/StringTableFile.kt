package com.flipperplz.bisutils.stringtable.ast


interface StringTableFile : List<StringTableLocalization> {
    val localizations: List<StringTableLocalization>
    val languages: List<StringTableLanguage>
}