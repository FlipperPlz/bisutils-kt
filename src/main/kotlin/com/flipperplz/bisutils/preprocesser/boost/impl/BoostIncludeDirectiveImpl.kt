package com.flipperplz.bisutils.preprocesser.boost.impl

import com.flipperplz.bisutils.parsing.BisLexer
import com.flipperplz.bisutils.preprocesser.boost.BoostPreprocessor
import com.flipperplz.bisutils.preprocesser.boost.directive.BoostIncludeDirective

class BoostIncludeDirectiveImpl(
        override val processor: BoostPreprocessor
) : BoostIncludeDirective {
    override lateinit var stringType: BoostIncludeDirective.StringType
    override lateinit var path: String

    constructor(lexer: BisLexer, processor: BoostPreprocessor): this(processor) {
        parseDirective(lexer)
    }

    override fun parseDirective(lexer: BisLexer) {
        BoostPreprocessor.traverseWhitespace(lexer)
        stringType = BoostIncludeDirective.StringType.detectType(lexer) ?: throw BoostPreprocessor.preprocessorException(lexer)
        path = stringType.readRaw(lexer)
    }
}