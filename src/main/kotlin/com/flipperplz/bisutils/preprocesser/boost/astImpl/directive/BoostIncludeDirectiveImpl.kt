package com.flipperplz.bisutils.preprocesser.boost.astImpl.directive

import com.flipperplz.bisutils.parsing.BisLexer
import com.flipperplz.bisutils.preprocesser.boost.BoostPreprocessor
import com.flipperplz.bisutils.preprocesser.boost.ast.directive.BoostIncludeDirective

class BoostIncludeDirectiveImpl(lexer: BisLexer, override val processor: BoostPreprocessor) : BoostIncludeDirective {
    override lateinit var stringType: BoostIncludeDirective.StringType
        private set
    override lateinit var path: String
        private set
    init {
        BoostPreprocessor.traverseWhitespace(lexer)
        stringType = BoostIncludeDirective.StringType.detectType(lexer) ?: throw BoostPreprocessor.preprocessorException(lexer)
        path = stringType.readRaw(lexer)
    }
}