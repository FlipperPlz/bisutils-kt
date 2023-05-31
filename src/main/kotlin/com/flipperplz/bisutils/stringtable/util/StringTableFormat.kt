package com.flipperplz.bisutils.stringtable.util

enum class StringTableFormat {
    CSV,
    XML,
    BIN;
    companion object {
        fun forExtension(extension: String): StringTableFormat? = when(extension) {
            "bin" -> StringTableFormat.BIN
            "xml" -> StringTableFormat.XML
            "csv" -> StringTableFormat.CSV
            else -> null
        }
    }
}