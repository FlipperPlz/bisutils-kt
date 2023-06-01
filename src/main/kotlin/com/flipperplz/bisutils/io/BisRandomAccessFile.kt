package com.flipperplz.bisutils.io

import java.io.File
import java.io.RandomAccessFile

class BisRandomAccessFile(val file: File, mode: String) : RandomAccessFile(file, mode) {
    val fileName = file.nameWithoutExtension
}