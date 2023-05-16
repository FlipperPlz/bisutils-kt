package com.flipperplz.bisutils

import com.flipperplz.bisutils.utils.BisLexer

interface BisPreProcessor {
    fun processText(lexer: BisLexer): Boolean {
        //TODO
        lexer.swapBuffers()
        lexer.clearTokenBuffer()
        return true
    }
}