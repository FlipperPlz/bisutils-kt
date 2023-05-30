package com.flipperplz.bisutils.preprocesser

import com.flipperplz.bisutils.parsing.BisLexer
import com.flipperplz.bisutils.parsing.LexerException

interface BisPreprocessor<T: BisLexer> {
    @Throws(LexerException::class)
    fun processLexer(lexer: T)
}