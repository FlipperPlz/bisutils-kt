package com.flipperplz.bisutils.parsing

enum class LexicalError(val errorId: String, private val message: String) {
    PrematureFileEnd("0x0001", "Premature end of file."),
    PrematureLineEnd("0x0002", "Premature end of line encountered."),
    UnexpectedInput("0x0003", "Unexpected input."),
    //ParamFile
    PreprocessorError("0xFF01", "Preprocessing failed.");

    override fun toString(): String = "$errorId: $message"
}