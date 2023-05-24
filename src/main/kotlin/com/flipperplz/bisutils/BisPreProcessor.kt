package com.flipperplz.bisutils

import com.flipperplz.bisutils.parsing.BisLexer

interface BisPreProcessor<T: BisLexer> {
    fun processText(lexer: T)
}