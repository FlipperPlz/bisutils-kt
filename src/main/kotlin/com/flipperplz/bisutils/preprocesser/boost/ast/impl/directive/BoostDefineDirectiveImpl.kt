package com.flipperplz.bisutils.preprocesser.boost.ast.impl.directive

import com.flipperplz.bisutils.parsing.BisLexer
import com.flipperplz.bisutils.preprocesser.boost.BoostPreprocessor
import com.flipperplz.bisutils.preprocesser.boost.ast.directive.BoostDefineDirective
import com.flipperplz.bisutils.preprocesser.boost.ast.impl.element.BoostMacroElementImpl

class BoostDefineDirectiveImpl(
    lexer: BisLexer, override val processor: BoostPreprocessor
): BoostDefineDirective {
    override lateinit var macroName: String
        private set
    override lateinit var macroArguments: List<String>
        private set
    override lateinit var macroValue: String
        private set
    init {
        BoostPreprocessor.traverseWhitespace(lexer)
        macroName = BoostMacroElementImpl.readMacroID(lexer, true)
        BoostPreprocessor.traverseWhitespace(lexer, true)
        when(lexer.currentChar) {
            null -> {}
            '(' -> {
                lexer.moveForward(); BoostPreprocessor.traverseWhitespace(lexer, false)
                val args = mutableListOf<String>()
                while (lexer.currentChar != ')') {
                    BoostPreprocessor.traverseWhitespace(lexer, false, allowEOL = true, allowDirectiveEOL = true)
                    args.add(BoostMacroElementImpl.readMacroID(lexer))
                    BoostPreprocessor.traverseWhitespace(lexer, false, allowEOL = true, allowDirectiveEOL = true)
                    if(with(lexer.currentChar) { this != ')' && this !=',' }) throw BoostPreprocessor.preprocessorException(lexer)
                    if(lexer.currentChar == ',')  lexer.moveForward()
                }
                BoostPreprocessor.traverseWhitespace(lexer, false, allowEOL = false, allowDirectiveEOL = true)
                macroArguments = args
            }
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