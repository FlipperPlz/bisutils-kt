package com.flipperplz.bisutils.stringtable.util

import com.flipperplz.bisutils.preprocesser.boost.BoostPreprocessor
import com.flipperplz.bisutils.stringtable.ast.StringTableFile
import com.flipperplz.bisutils.stringtable.lexer.StringTableLexer
import com.flipperplz.bisutils.stringtable.parser.StringTableCSVParser
import java.nio.ByteBuffer
import java.nio.charset.Charset

fun openStringtable(
    byteBuffer: ByteBuffer,
    format: StringTableFormat = StringTableFormat.CSV,
    preprocessor: BoostPreprocessor? = null,
    charset: Charset = Charsets.UTF_8
): StringTableFile = when(format) {
    StringTableFormat.CSV -> StringTableCSVParser.parse(StringTableLexer(byteBuffer, charset), preprocessor)
    StringTableFormat.BIN -> TODO()
    StringTableFormat.XML -> TODO()
}

