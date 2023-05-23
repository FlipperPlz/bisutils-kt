package com.flipperplz.bisutils.param.parser

import com.flipperplz.bisutils.BisPreProcessor
import com.flipperplz.bisutils.param.lexer.ParamLexer
import com.flipperplz.bisutils.parsing.LexerException

class ParamPreProcessor : BisPreProcessor<ParamLexer> {
    @Throws(LexerException::class)
    override fun processText(lexer: ParamLexer): ParamLexer {
        TODO("Not yet implemented")
    }
}