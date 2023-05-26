package com.flipperplz.bisutils.preprocesser.boost.utils

import com.flipperplz.bisutils.parsing.BisLexer
import com.flipperplz.bisutils.parsing.LexerException
import com.flipperplz.bisutils.parsing.LexicalError
import com.flipperplz.bisutils.preprocesser.boost.BoostPreprocessor
import com.flipperplz.bisutils.preprocesser.boost.BoostPreprocessor.Companion.preprocessorException
import com.flipperplz.bisutils.preprocesser.boost.BoostPreprocessor.Companion.traverseWhitespace
import com.flipperplz.bisutils.preprocesser.boost.directive.*
import com.flipperplz.bisutils.preprocesser.boost.impl.*

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
        B_ENDIF -> BoostEndIfDirectiveImpl(lexer, processor)
        B_ELSE -> BoostElseDirectiveImpl(lexer, processor)
        B_INCLUDE -> BoostIncludeDirectiveImpl(lexer, processor)
        B_UNDEFINE -> BoostUndefineDirectiveImpl(lexer, processor)
        B_DEFINE -> BoostDefineDirectiveImpl(lexer, processor)
        else -> throw preprocessorException(lexer)
    }





    companion object {


        fun directiveForKeyword(keyword: String): BoostDirectiveType? = values().firstOrNull { it.text == keyword }
    }
}