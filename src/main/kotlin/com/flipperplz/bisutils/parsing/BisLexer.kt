package com.flipperplz.bisutils.parsing

open class BisLexer(private var string: String) {
    var bufferPtr: Int = -1
        private set
    var currentChar: Char? = null
        private set
    var previousChar: Char? = null
        private set
    fun hasNext(): Boolean = peekForward() != null

    fun isEOF(): Boolean =  currentChar == null && bufferPtr > string.length

    fun moveForward(count: Int = 1): Char? = jumpTo(bufferPtr + count)
    fun moveBackward(count: Int = 1) : Char? = jumpTo(bufferPtr - count)

    fun resetPosition() {
        bufferPtr = -1
        previousChar = null
        currentChar = null
    }

    fun eofException(): LexerException = LexerException(this, LexicalError.PrematureFileEnd)

    fun eolException(): LexerException = LexerException(this, LexicalError.PrematureLineEnd)

    fun unexpectedInputException(): LexerException = LexerException(this, LexicalError.UnexpectedInput)

    fun jumpTo(position: Int): Char? {
        bufferPtr = position
        previousChar = string.getOrNull(bufferPtr - 1)
        return string.getOrNull(bufferPtr).also {
            currentChar = it
        }
    }

    fun removeRange(from: Int, to: Int) {
        string = string.removeRange(from, to)
    }

    fun removeRange(range: IntRange) {
        string = string.removeRange(range)
    }

    fun readChars(count: Int, includeFirst: Boolean = false): String {
        var i = 0; val builder = StringBuilder()
        if(includeFirst && count >= 1) { i++; builder.append(currentChar) }
        while (i != count) { i++;builder.append(moveForward()) }
        return builder.toString()
    }

    fun peekForward(): Char? = string.getOrNull(bufferPtr + 1)

    fun peekBackward(): Char? = string.getOrNull(bufferPtr - 1)

    fun peekAt(pos: Int): Char? = string.getOrNull(pos)

    fun regionMatches(text: String): Boolean = string.regionMatches(bufferPtr, text, 0, text.length)

    fun replaceAll(from: String, to: String) { string = string.replace(from, to) }

    fun shoveText(text: String): Int = "${string.substring(0, bufferPtr)}$text${string.substring(bufferPtr)}".let {
        bufferPtr + it.length
    }
}