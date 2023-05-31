package com.flipperplz.bisutils.preprocesser

import com.flipperplz.bisutils.parsing.BisLexer
import com.flipperplz.bisutils.parsing.LexerException

interface BisPreprocessor {
    @Throws(LexerException::class)
    fun processLexer(lexer: BisLexer)
    fun processAndReset(lexer: BisLexer) = with(lexer)  {
        val start = bufferPtr
        processLexer(this)
        jumpTo(start)
    }
}