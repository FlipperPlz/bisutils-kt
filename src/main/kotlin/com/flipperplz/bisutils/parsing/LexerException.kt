package com.flipperplz.bisutils.parsing


data class LexerException(
    val lexer: BisLexer,
    val errorType: LexicalError,
) : Exception() {
    override val message: String = errorType.toString()
}