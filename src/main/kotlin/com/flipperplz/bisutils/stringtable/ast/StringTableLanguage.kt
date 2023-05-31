package com.flipperplz.bisutils.stringtable.ast


sealed interface StringTableLanguage {
    companion object {
        private val languages: MutableMap<String, StringTableLanguage> = mutableMapOf<String, StringTableLanguage>().apply {
            put("English", English)
            put("Original", Original)
        }
    }
    val languageName: String

    fun custom(name: String): StringTableLanguage = languages[name] ?: Custom(name)

    object Original : StringTableLanguage {
        override val languageName: String = "Original"
    }

    object English : StringTableLanguage {
        override val languageName: String = "Original"
    }

    private class Custom(override val languageName: String) : StringTableLanguage {
        init {
            languages.putIfAbsent(languageName, this)
        }
    }
}