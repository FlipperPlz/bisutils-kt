package com.flipperplz.bisutils.preprocesser.boost.impl

import com.flipperplz.bisutils.parsing.BisLexer
import com.flipperplz.bisutils.preprocesser.boost.BoostPreprocessor
import com.flipperplz.bisutils.preprocesser.boost.directive.BoostDefineDirective

class BoostDefineDirectiveImpl(
        override val processor: BoostPreprocessor
): BoostDefineDirective {
    override lateinit var macroName: String
    override lateinit var macroArguments: List<String>
    override lateinit var macroValue: String


    constructor(lexer: BisLexer, processor: BoostPreprocessor): this(processor) {
        parseDirective(lexer)
    }

    override fun parseDirective(lexer: BisLexer) {
        BoostPreprocessor.traverseWhitespace(lexer)
        val name = BoostPreprocessor.readMacroID(lexer, true)
        BoostPreprocessor.traverseWhitespace(lexer, true)
        if(lexer.currentChar == null) return
        when(lexer.currentChar) {
            null -> return
            '(' -> {
                lexer.moveForward(); BoostPreprocessor.traverseWhitespace(lexer, false)
                val args = mutableListOf<String>()
                while (lexer.currentChar != ')') {
                    BoostPreprocessor.traverseWhitespace(lexer, false, allowEOL = true, allowDirectiveEOL = true)
                    args.add(BoostPreprocessor.readMacroID(lexer))
                    BoostPreprocessor.traverseWhitespace(lexer, false, allowEOL = true, allowDirectiveEOL = true)
                    if(with(lexer.currentChar) { this != ')' && this !=',' }) throw BoostPreprocessor.preprocessorException(lexer)
                    if(lexer.currentChar == ',')  lexer.moveForward()
                }
                lexer.moveForward()
                BoostPreprocessor.traverseWhitespace(lexer, false, allowEOL = true, allowDirectiveEOL = true)

                val definition = StringBuilder()
                while (lexer.currentChar != '\n' && lexer.currentChar != '\r') {
                    when(lexer.currentChar) {
                        '\\' -> {
                            if(lexer.moveForward() == '\r' ) {
                                if(lexer.moveForward( ) != '\n') throw BoostPreprocessor.preprocessorException(lexer)
                                definition.append("\n")
                            } else if(lexer.currentChar != '\n') {
                                definition.append('\\').append(lexer.currentChar)
                            } else definition.append('\\')
                        }
                        else -> definition.append(lexer.currentChar)
                    }
                    lexer.moveForward()
                }

                macroValue = definition.toString()
            }
        }
    }
}