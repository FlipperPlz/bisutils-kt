package com.flipperplz.bisutils.preprocesser.boost.utils

import com.flipperplz.bisutils.parsing.BisLexer
import com.flipperplz.bisutils.parsing.LexerException
import com.flipperplz.bisutils.parsing.LexicalError
import com.flipperplz.bisutils.preprocesser.boost.BoostPreprocessor
import com.flipperplz.bisutils.preprocesser.boost.BoostPreprocessor.Companion.preprocessorException
import com.flipperplz.bisutils.preprocesser.boost.directive.BoostElseDirective
import com.flipperplz.bisutils.preprocesser.boost.directive.BoostEndIfDirective
import com.flipperplz.bisutils.preprocesser.boost.directive.BoostIncludeDirective
import com.flipperplz.bisutils.preprocesser.boost.directive.BoostUndefineDirective

enum class BoostDirectiveType(val debugName: String, val text: String) {
    B_INCLUDE("boost::include", "include"),
    B_DEFINE("boost::define", "define"),
    B_UNDEFINE("boost::undefine", "undef"),
    B_IF("boost::if", "if"),
    B_IFDEF("boost::if.defined", "ifdef"),
    B_IFNDEF("boost::if.not-defined", "ifndef"),
    B_ELSE("boost::else", "else"),
    B_ENDIF("boost::endif", "endif");

    fun parse(lexer: BisLexer): BoostDirective = when(this) {
        B_ENDIF -> object : BoostEndIfDirective{
            override fun process(arg: Any?): String {
                return super.process(arg)
            }
        }
        B_ELSE -> object : BoostElseDirective{
            override fun process(arg: Any?): String {
                return super.process(arg)
            }
        }
        B_INCLUDE -> object : BoostIncludeDirective {
            override lateinit var stringType: BoostIncludeDirective.StringType
            override lateinit var path: String
            init {
                parseDirective(lexer)
            }

            override fun process(arg: Any?): String {
                return super.process(arg)
            }

            override fun parseDirective(lexer: BisLexer) {
                BoostPreprocessor.traverseWhitespace(lexer)
                stringType = BoostIncludeDirective.StringType.detectType(lexer) ?: throw preprocessorException(lexer)
                path = stringType.readRaw(lexer)
            }
        }
        B_UNDEFINE -> object : BoostUndefineDirective {
            override lateinit var macroName: String
            init { parseDirective(lexer) }

            override fun process(arg: Any?): String {
                return super.process(arg)
            }

            override fun parseDirective(lexer: BisLexer) {
                BoostPreprocessor.traverseWhitespace(lexer)
                macroName = lexer.getWhile { it.currentChar?.isLetterOrDigit() ?: false }
            }

        }
        else -> throw preprocessorException(lexer)
    }




    companion object {
        fun directiveForKeyword(keyword: String): BoostDirectiveType? = values().firstOrNull { it.text == keyword }
    }
}