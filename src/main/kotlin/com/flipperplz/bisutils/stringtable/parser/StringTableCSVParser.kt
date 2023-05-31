package com.flipperplz.bisutils.stringtable.parser

import com.flipperplz.bisutils.param.utils.extensions.mutableStringTable
import com.flipperplz.bisutils.parsing.BisLexer
import com.flipperplz.bisutils.preprocesser.BisPreprocessor
import com.flipperplz.bisutils.preprocesser.boost.BoostPreprocessor
import com.flipperplz.bisutils.stringtable.lexer.StringTableLexer
import com.flipperplz.bisutils.stringtable.ast.StringTableFile
import com.flipperplz.bisutils.stringtable.ast.StringTableLanguage

object StringTableCSVParser {
    fun parse(lexer: StringTableLexer, preProcessor: BisPreprocessor? = null): StringTableFile = mutableStringTable().apply {
        preProcessor?.processAndReset(lexer)
        var ln = 0

        var keyword = lexer.readCSVColumn()
        if(keyword.isNotBlank() && keyword.equals("Language", true)) {
            while (lexer.currentChar == ',') {
                lexer.moveForward()
                keyword = lexer.readCSVColumn()
                languages.add(StringTableLanguage.forName(keyword))
            }
        } else return@apply
        BoostPreprocessor.traverseWhitespace(lexer, allowEOF = true, allowEOL = true, allowDirectiveEOL = true)
    }
}