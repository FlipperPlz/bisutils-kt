package com.flipperplz.bisutils.utils

import java.nio.ByteBuffer

class BisLexer(
    private var string: String
) {
    var tokenBuffer: MutableList<Char> = mutableListOf()
        private set
    var bufferPtr: Int = -1
        private set
    var currentChar: Char = Char.MIN_VALUE
        private set
    var previousChar: Char = Char.MIN_VALUE
        private set
    val lineWidths: MutableList<Int> = mutableListOf()
    var line: Int = 0
        private set
    var col: Int = 0
        private set

    fun moveForward(): Char {
        val char = currentChar
        bufferPtr++
        previousChar = currentChar
        currentChar = if(bufferPtr < string.length)
            string[bufferPtr] else Char.MIN_VALUE

        incrementCurrent()
        return currentChar
    }

    fun getPositionstring(): String = "$line:$col"

    fun pushToTokenBuffer() {
        tokenBuffer.add(currentChar)
    }

    fun clearTokenBuffer() {
        tokenBuffer.clear()
    }

    fun resetPosition() {
        bufferPtr = 0
        previousChar = Char.MIN_VALUE
        currentChar = string.firstOrNull() ?: Char.MIN_VALUE
    }

    fun swapBuffers() {
        val currentBuffer = string.toMutableList()
        string = tokenBuffer.toString()
        tokenBuffer = currentBuffer
        resetPosition()
    }

    fun pushToTokenBuffer(char: Char) {
        tokenBuffer.add(char)
    }

    fun pushToTokenBuffer(string: String) {
        tokenBuffer.addAll(string.toList())
    }

    fun moveBackward() : Char {
        bufferPtr--
        currentChar = previousChar
        previousChar = if (bufferPtr > 0)
            string.getOrNull(bufferPtr - 1) ?: Char.MIN_VALUE else Char.MIN_VALUE

        decrementCurrent()

        return currentChar
    }


    fun jumpTo(position: Int) {
        bufferPtr = position
        currentChar = string.getOrElse(bufferPtr) { Char.MIN_VALUE }
        previousChar = string.getOrElse(bufferPtr - 1) { Char.MIN_VALUE }
    }

    fun isEOL(): Boolean = currentChar == '\n'

    fun isEOF(): Boolean = bufferPtr >= string.length

    fun isWhitespace(): Boolean = Companion.isWhitespace(currentChar)

    fun isAlphaNumeric(): Boolean = currentChar.isLetterOrDigit()

    inline fun getWhile(condition: (BisLexer) -> Boolean): String {
        val builder = StringBuilder()
        while (true) {
            moveForward()
            if(!condition(this)) break
            builder.append(currentChar)
        }
        moveBackward()
        return builder.toString()
    }


    inline fun skipWhile(condition: (BisLexer) -> Boolean) {
        while (condition(this)) moveForward()
    }

    inline fun getUntil(condition: (BisLexer) -> Boolean): String =
        getWhile { !condition(this)}

    fun peekForward(): Char = string.getOrNull(bufferPtr + 1) ?: Char.MIN_VALUE

    fun peekBackward(): Char = string.getOrNull(bufferPtr - 1) ?: Char.MIN_VALUE

    fun peekAt(pos: Int) = string[pos]

    private fun decrementCurrent() {
        if (currentChar == '\n') {
            line--
            col = lineWidths[line]
        }
        else col--
    }


    private fun incrementCurrent() {
        if (currentChar == '\n') {
            lineWidths[line] = col - 1
            line++
            col = 0
        } else {
            col++
        }
    }

    companion object {
        fun isWhitespace(currentChar: Char): Boolean = with(currentChar.code) {
            this == 0x20 ||
            this == 0x0c ||
            this == 0x0a ||
            this == 0x0d ||
            this == 0x09 ||
            this == 0x0b
        }
    }
}