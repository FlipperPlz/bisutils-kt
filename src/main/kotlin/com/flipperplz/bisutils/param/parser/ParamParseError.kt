package com.flipperplz.bisutils.param.parser

import com.flipperplz.bisutils.utils.BisLexer

enum class ParamParseError(
    val errorId: String,
    private val message: String
) {
    PrematureFileEnd(
        "0x0001",
        "Premature end of file."
    ),
    PrematureLineEnd(
        "0x0002",
        "Premature end of line encountered."
    ),
    UnexpectedInput(
        "0x0003",
        "Unexpected input %CChar%."
    );

    fun getMessage(
        lexer: BisLexer,
        context: String?,
        customMessage: String?,
        expectedWords: List<String> = emptyList()
    ): String {
        val builder = StringBuilder("[${lexer.line}:${lexer.col} {${lexer.bufferPtr}}] Error $errorId")
        if(!context.isNullOrBlank()) builder.append("::").append(context)

        builder.append(" - ")
        builder.append(message.replace("%CChar%", "'${lexer.currentChar}'").replace("%PChar%", "'${lexer.previousChar}'"))
        if(expectedWords.isNotEmpty()) {
            builder.append(" Expected to find one of the following: (")
            builder.append(expectedWords.joinToString(postfix = ").") { "'$it'" })
        }
        if(!customMessage.isNullOrBlank()) builder.append(' ').append(customMessage)
        return builder.toString()
    }

}