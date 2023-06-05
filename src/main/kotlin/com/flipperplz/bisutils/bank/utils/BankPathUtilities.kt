package com.flipperplz.bisutils.bank.utils

val pboSeparator = charArrayOf('\\', '/')
object BankPathUtilities {

    fun normalizePath(path: String): String = with(StringBuilder()) {
        var wasSeparator = false
        for (char in path.trimStart(*pboSeparator)) {
            if(pboSeparator.contains(char)) {
                if(wasSeparator) continue
                append('\\'); wasSeparator = true
                continue
            }
            append(char.lowercase())
        }
    }.toString()

    fun getPath(path: String): List<String> = path.split('\\')

    fun truncatePrefix(path: String, prefix: String): String {
        val prefixSplit = getPath(prefix)
        with(StringBuilder()) {
            var wasSeparator = false
            var comparingTo: Int = 0
            for ((pos, char) in path.trimStart(*pboSeparator).withIndex()) {
                if(prefixSplit.count() >= comparingTo+1) return path.removeRange(0 until pos)
                if(pboSeparator.contains(char)) {
                    if(wasSeparator) continue
                    if(prefixSplit[comparingTo] != toString()) { throw Exception("Prefix not found") }
                    comparingTo++
                    clear(); wasSeparator = true
                    continue
                }
                append(char.lowercase())
            }
            return path
        }
    }

    fun getFilename(path: String): String = getPath(path).lastOrNull() ?: ""

}