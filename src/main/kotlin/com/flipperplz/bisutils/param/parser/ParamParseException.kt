package com.flipperplz.bisutils.param.parser

import com.flipperplz.bisutils.utils.BisLexer

data class ParamParseException(
    val lexer: BisLexer,
    val errorType: ParamParseError,
    val context: String? = null,
    val customMessage: String? = null,
    val expectedWords: List<String>? = null
) : Exception(errorType.getMessage(lexer, context, customMessage, expectedWords ?: emptyList()))