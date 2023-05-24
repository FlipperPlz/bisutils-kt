package com.flipperplz.bisutils

import com.flipperplz.bisutils.parsing.BisLexer
import com.flipperplz.bisutils.parsing.LexerException

interface BisPreProcessor<T: BisLexer> {
    @Throws(LexerException::class)
    fun processText(lexer: T)
}