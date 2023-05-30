package com.flipperplz.bisutils.preprocesser.boost.impl

import com.flipperplz.bisutils.parsing.BisLexer
import com.flipperplz.bisutils.preprocesser.boost.BoostPreprocessor
import com.flipperplz.bisutils.preprocesser.boost.directive.BoostElseDirective

class BoostElseDirectiveImpl(
    override val processor: BoostPreprocessor
) : BoostElseDirective {

    constructor(lexer: BisLexer, processor: BoostPreprocessor): this(processor) {
        parseDirective(lexer)
    }

}