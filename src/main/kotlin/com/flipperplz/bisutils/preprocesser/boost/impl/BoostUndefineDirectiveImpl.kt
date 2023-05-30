package com.flipperplz.bisutils.preprocesser.boost.impl

import com.flipperplz.bisutils.parsing.BisLexer
import com.flipperplz.bisutils.preprocesser.boost.BoostPreprocessor
import com.flipperplz.bisutils.preprocesser.boost.directive.BoostUndefineDirective

class BoostUndefineDirectiveImpl(lexer: BisLexer, override val processor: BoostPreprocessor) : BoostUndefineDirective {
    override lateinit var macroName: String
        private set
    init {
        BoostPreprocessor.traverseWhitespace(lexer)
        macroName = lexer.getWhile { it.currentChar?.isLetterOrDigit() ?: false }
    }
}