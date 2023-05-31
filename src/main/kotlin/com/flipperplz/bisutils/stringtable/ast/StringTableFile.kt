package com.flipperplz.bisutils.stringtable.ast


interface StringTableFile {
    val localizations: List<StringTableLocalization>
    val languages: List<StringTableLanguage>
}