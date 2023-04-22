package com.flipperplz.bisutils

import com.flipperplz.bisutils.rap.io.BisRapDebinarizer
import com.flipperplz.bisutils.rap.io.BisRapWriter
import com.flipperplz.bisutils.rap.io.formatting.BisRapBeautifier
import java.io.File
import java.io.FileInputStream

fun main() {
    val file = BisRapDebinarizer.debinarizeFile(File("E:\\dayz\\example.bin")) ?: return
    BisRapWriter.writeTo(file, File("C:\\Users\\developer\\Desktop\\ai.pbo"))
}