package com.flipperplz.bisutils.preprocesser.boost.utils

import com.flipperplz.bisutils.parsing.BisLexer
import com.flipperplz.bisutils.preprocesser.boost.BoostPreprocessor
import com.flipperplz.bisutils.preprocesser.boost.ast.directive.BoostDirective
import com.flipperplz.bisutils.preprocesser.boost.ast.directive.*
import com.flipperplz.bisutils.preprocesser.boost.astImpl.directive.*

enum class BoostDirectiveType(val debugName: String, val text: String) {
    B_INCLUDE("boost::include", "include") {
        override fun parse(lexer: BisLexer, processor: BoostPreprocessor): BoostIncludeDirective =
            BoostIncludeDirectiveImpl(lexer, processor)
    }, B_DEFINE("boost::define", "define") {
        override fun parse(lexer: BisLexer, processor: BoostPreprocessor): BoostDefineDirective =
            BoostDefineDirectiveImpl(lexer, processor)
    }, B_UNDEFINE("boost::undefine", "undef") {
        override fun parse(lexer: BisLexer, processor: BoostPreprocessor): BoostUndefineDirective =
            BoostUndefineDirectiveImpl(lexer, processor)
    }, B_IFDEF("boost::if.defined", "ifdef") {
        override fun parse(lexer: BisLexer, processor: BoostPreprocessor): BoostIfDefinedDirective =
            BoostIfDefinedDirectiveImpl(lexer, processor)
    }, B_IFNDEF("boost::if.not-defined", "ifndef") {
        override fun parse(lexer: BisLexer, processor: BoostPreprocessor): BoostIfNDefinedDirective =
            BoostIfNDefinedDirectiveImpl(lexer, processor)
    };

    open fun parse(lexer: BisLexer, processor: BoostPreprocessor): BoostDirective? = null

    companion object {
        private fun directiveForKeyword(keyword: String): BoostDirectiveType? = values().firstOrNull { it.text == keyword }
        private fun directiveFromLexer(lexer: BisLexer): BoostDirectiveType? = BoostDirectiveType.directiveForKeyword(StringBuilder().apply {
            while(lexer.moveForward()?.isWhitespace() != true) append(lexer.currentChar)
        }.toString())

        fun parse(lexer: BisLexer, processor: BoostPreprocessor): BoostDirective? = directiveForKeyword(StringBuilder().apply {
            while(lexer.moveForward()?.isWhitespace() != true) append(lexer.currentChar)
        }.toString())?.parse(lexer, processor)
    }
}