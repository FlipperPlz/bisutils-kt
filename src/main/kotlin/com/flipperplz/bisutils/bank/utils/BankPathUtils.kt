package com.flipperplz.bisutils.bank.utils

object BankPathUtils {

    fun normalizePboPath(path: String): String =
        path.lowercase().replace(Regex("[\\\\/]+"), "\\\\").trimEnd('\\')

    fun getFilename(path: String): String =
        path.removeRange(0..path.lastIndexOf('\\'))

    fun getParent(path: String): String =
        path.removeRange(path.lastIndexOf('\\')+1..path.length)
}