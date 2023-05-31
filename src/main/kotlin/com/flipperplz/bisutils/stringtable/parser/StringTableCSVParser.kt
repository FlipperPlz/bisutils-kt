package com.flipperplz.bisutils.stringtable.parser

import com.flipperplz.bisutils.param.utils.extensions.mutableStringTable
import com.flipperplz.bisutils.preprocesser.BisPreprocessor
import com.flipperplz.bisutils.preprocesser.boost.BoostPreprocessor
import com.flipperplz.bisutils.stringtable.lexer.StringTableLexer
import com.flipperplz.bisutils.stringtable.ast.StringTableFile
import com.flipperplz.bisutils.stringtable.ast.StringTableLanguage
import com.flipperplz.bisutils.stringtable.astImpl.mutable.StringTableMutableLocalizationImpl

object StringTableCSVParser {
    fun parse(lexer: StringTableLexer, preProcessor: BisPreprocessor? = null): StringTableFile = mutableStringTable().apply {
        preProcessor?.processAndReset(lexer)

        var keyword = lexer.readCSVColumn()
        if(keyword.isNotBlank() && keyword.equals("Language", true)) {
            while (lexer.currentChar == ',') {
                lexer.moveForward()
                keyword = lexer.readCSVColumn()
                languages.add(StringTableLanguage.forName(keyword))
            }
        } else return@apply

        while (lexer.isEOF()) {
            val localization = StringTableMutableLocalizationImpl(lexer.readCSVColumn())

            var col = 0
            do {
                languages.getOrNull(col).let {
                    if(it == null) throw Exception("Unknown language for col $col")
                    localization.definitions.putIfAbsent(languages[col], lexer.readCSVColumn())
                    col++
                }
            } while (lexer.currentChar == ',')
            localizations.add(localization)
            BoostPreprocessor.traverseWhitespace(lexer, allowEOF = true, allowEOL = true, allowDirectiveEOL = true)
        }
        BoostPreprocessor.traverseWhitespace(lexer, allowEOF = true, allowEOL = true, allowDirectiveEOL = true)
    }
}