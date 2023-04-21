package com.flipperplz.bisutils

import com.flipperplz.bisutils.rap.io.BisRapDebinarizer
import java.io.File
import java.io.FileInputStream

fun main() {
    val file = BisRapDebinarizer.debinarizeFile(File("E:\\dayz\\config.bin"))
    println("done.")
}