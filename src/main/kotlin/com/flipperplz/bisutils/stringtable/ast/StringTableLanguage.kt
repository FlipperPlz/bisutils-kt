package com.flipperplz.bisutils.stringtable.ast


sealed interface StringTableLanguage {
    companion object {
        private val languages: MutableMap<String, StringTableLanguage> = mutableMapOf<String, StringTableLanguage>().apply {
            put("english", English)
            put("original", Original)
        }

        fun forName(name: String, ignoreCase: Boolean = true): StringTableLanguage =
            languages[if(ignoreCase) name.lowercase() else name] ?: Custom(name.lowercase())

    }
    val languageName: String


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