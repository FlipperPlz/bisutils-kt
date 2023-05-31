package com.flipperplz.bisutils.stringtable.ast

interface StringTableLocalization {
    val name: String
    val definitions: Map<StringTableLanguage, String>
    val parsedFrom: StringTableFile?
    val original: String
        get() = definitions[StringTableLanguage.Original] ?: definitions.values.first()

    infix fun forLanguage(language: StringTableLanguage): String? =
        definitions[language]

    infix fun forLanguageOrDefault(language: StringTableLanguage): String? =
        forLanguage(language) ?: original
}