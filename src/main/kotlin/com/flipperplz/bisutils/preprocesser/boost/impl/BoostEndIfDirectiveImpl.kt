package com.flipperplz.bisutils.preprocesser.boost.impl

import com.flipperplz.bisutils.parsing.BisLexer
import com.flipperplz.bisutils.preprocesser.boost.BoostPreprocessor
import com.flipperplz.bisutils.preprocesser.boost.directive.BoostEndIfDirective

class BoostEndIfDirectiveImpl(
        override val processor: BoostPreprocessor
) : BoostEndIfDirective {
    constructor(lexer: BisLexer, processor: BoostPreprocessor): this(processor) {
        parseDirective(lexer)
    }

    override fun process(arg: Any?): String = "//endif"
}