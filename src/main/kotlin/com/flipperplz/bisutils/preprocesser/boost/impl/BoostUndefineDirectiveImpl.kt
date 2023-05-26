package com.flipperplz.bisutils.preprocesser.boost.impl

import com.flipperplz.bisutils.parsing.BisLexer
import com.flipperplz.bisutils.preprocesser.boost.BoostPreprocessor
import com.flipperplz.bisutils.preprocesser.boost.directive.BoostUndefineDirective

class BoostUndefineDirectiveImpl(override val processor: BoostPreprocessor) : BoostUndefineDirective {
    override lateinit var macroName: String

    constructor(lexer: BisLexer, processor: BoostPreprocessor): this(processor) {
        parseDirective(lexer)
    }

    override fun parseDirective(lexer: BisLexer) {
        BoostPreprocessor.traverseWhitespace(lexer)
        macroName = lexer.getWhile { it.currentChar?.isLetterOrDigit() ?: false }
    }

}