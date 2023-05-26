package com.flipperplz.bisutils.preprocesser.boost.utils

import com.flipperplz.bisutils.parsing.BisLexer
import com.flipperplz.bisutils.parsing.LexerException
import com.flipperplz.bisutils.parsing.LexicalError
import com.flipperplz.bisutils.preprocesser.boost.BoostPreprocessor
import com.flipperplz.bisutils.preprocesser.boost.BoostPreprocessor.Companion.preprocessorException
import com.flipperplz.bisutils.preprocesser.boost.BoostPreprocessor.Companion.traverseWhitespace
import com.flipperplz.bisutils.preprocesser.boost.directive.*

enum class BoostDirectiveType(val debugName: String, val text: String) {
    B_INCLUDE("boost::include", "include"),
    B_DEFINE("boost::define", "define"),
    B_UNDEFINE("boost::undefine", "undef"),
    B_IF("boost::if", "if"),
    B_IFDEF("boost::if.defined", "ifdef"),
    B_IFNDEF("boost::if.not-defined", "ifndef"),
    B_ELSE("boost::else", "else"),
    B_ENDIF("boost::endif", "endif");

    fun parse(lexer: BisLexer, processor: BoostPreprocessor): BoostDirective = when(this) {
        B_ENDIF -> object : BoostEndIfDirective{
            override val processor: BoostPreprocessor = processor
            override fun process(arg: Any?): String = "//endif"
        }
        B_ELSE -> object : BoostElseDirective{
            override val processor: BoostPreprocessor = processor
            override fun process(arg: Any?): String = "//else"
        }
        B_INCLUDE -> object : BoostIncludeDirective {
            override val processor: BoostPreprocessor = processor

            override lateinit var stringType: BoostIncludeDirective.StringType
            override lateinit var path: String
            init {
                parseDirective(lexer)
            }

            override fun parseDirective(lexer: BisLexer) {
                BoostPreprocessor.traverseWhitespace(lexer)
                stringType = BoostIncludeDirective.StringType.detectType(lexer) ?: throw preprocessorException(lexer)
                path = stringType.readRaw(lexer)
            }
        }
        B_UNDEFINE -> object : BoostUndefineDirective {
            override lateinit var macroName: String
            override val processor: BoostPreprocessor = processor
            init { parseDirective(lexer) }


            override fun parseDirective(lexer: BisLexer) {
                BoostPreprocessor.traverseWhitespace(lexer)
                macroName = lexer.getWhile { it.currentChar?.isLetterOrDigit() ?: false }
            }

        }
        B_DEFINE -> object : BoostDefineDirective {
            override lateinit var macroName: String
            override lateinit var macroArguments: List<String>
            override lateinit var macroValue: String
            override val processor: BoostPreprocessor = processor

            override fun parseDirective(lexer: BisLexer) {
                BoostPreprocessor.traverseWhitespace(lexer)
                val name = readMacroID(lexer, true)
                traverseWhitespace(lexer, true)
                if(lexer.currentChar == null) return
                when(lexer.currentChar) {
                    null -> return
                    '(' -> {
                        lexer.moveForward(); traverseWhitespace(lexer, false)
                        val args = mutableListOf<String>()
                        while (lexer.currentChar != ')') {
                            traverseWhitespace(lexer, false, allowEOL = true, allowDirectiveEOL = true)
                            args.add(readMacroID(lexer))
                            traverseWhitespace(lexer, false, allowEOL = true, allowDirectiveEOL = true)
                            if(with(lexer.currentChar) { this != ')' && this !=',' }) throw preprocessorException(lexer)
                            if(lexer.currentChar == ',')  lexer.moveForward()
                        }
                        lexer.moveForward()
                        traverseWhitespace(lexer, false, allowEOL = true, allowDirectiveEOL = true)

                        val definition = StringBuilder()
                        while (lexer.currentChar != '\n' && lexer.currentChar != '\r') {
                            when(lexer.currentChar) {
                                '\\' -> {
                                    if(lexer.moveForward() == '\r' ) {
                                        if(lexer.moveForward( ) != '\n') throw preprocessorException(lexer)
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
        else -> throw preprocessorException(lexer)
    }


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


    companion object {
        fun directiveForKeyword(keyword: String): BoostDirectiveType? = values().firstOrNull { it.text == keyword }
    }
}