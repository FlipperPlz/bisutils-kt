package com.flipperplz.bisutils.preprocesser.boost.astImpl.element

import com.flipperplz.bisutils.parsing.BisLexer
import com.flipperplz.bisutils.preprocesser.boost.BoostPreprocessor
import com.flipperplz.bisutils.preprocesser.boost.BoostPreprocessor.Companion.preprocessorException
import com.flipperplz.bisutils.preprocesser.boost.ast.element.BoostMacroElement

data class BoostMacroElementImpl(
    override val processor: BoostPreprocessor,
    override val macroName: String,
    override val macroArguments: List<String>
) : BoostMacroElement {
    constructor(lexer: BisLexer, processor: BoostPreprocessor) : this(processor, readMacroID(lexer), readMacroArguments(lexer))

    companion object {
        fun readMacroID(lexer: BisLexer, throwOnNone: Boolean = false): String {
            if(with(lexer.currentChar) {this != null && isLetter() || this == '_' }) {
                return StringBuilder().apply {
                    append(lexer.currentChar)
                    while (with(lexer.moveForward()) { this != null && (this == '_' || this.isLetterOrDigit()) })
                        append(lexer.currentChar)
                }.toString()
            }
            if(throwOnNone) throw preprocessorException(lexer)
            return ""
        }

        private fun readMacroArguments(lexer: BisLexer): List<String> {
            if(lexer.currentChar != '(') return emptyList()
            lexer.moveForward(); BoostPreprocessor.traverseWhitespace(lexer, allowEOF = false, allowEOL = false, allowDirectiveEOL = false)
            return mutableListOf<String>().apply {
                val builder = StringBuilder()
                while (true) {
                    when(lexer.currentChar) {
                        ',' -> {
                            add(builder.toString())
                            builder.clear()
                        }
                        ')' -> break //TODO: Check parenthesis level for EXEC/EVAL
                        else -> builder.append(lexer.currentChar)
                    }
                    lexer.moveForward()
                }
            }

        }

    }
}