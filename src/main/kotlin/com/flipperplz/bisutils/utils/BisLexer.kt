package com.flipperplz.bisutils.utils

import java.nio.ByteBuffer

class BisLexer(
    private var string: String
) {
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

    fun resetPosition() {
        bufferPtr = 0
        previousChar = Char.MIN_VALUE
        currentChar = string.firstOrNull() ?: Char.MIN_VALUE
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

    fun isNextEOF() = bufferPtr+1 >= string.length

    fun isWhitespace(): Boolean = Companion.isWhitespace(currentChar)

    fun isAlphaNumeric(): Boolean = currentChar.isLetterOrDigit()

    inline fun getWhile(endOnLastChar: Boolean = false, condition: () -> Boolean): String {
        val builder = StringBuilder()
        do {
            moveForward()
            builder.append(moveForward())
        } while (condition() && with(builder.append(currentChar)) {true })
        if(!endOnLastChar) moveBackward()
        return builder.toString()
    }


    inline fun skipWhile(condition: () -> Boolean) {
        while (condition()) moveForward()
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
            lineWidths.add(line, col - 1)
            line++
            col = 0
        } else {
            col++
        }
    }

    fun nextIsWhitespace(): Boolean = isWhitespace(peekForward())

    fun traverseWhitespace(endOnWhitespace: Boolean = true): Boolean {
        if(!isWhitespace() && !isEOL()) return true
        while (with(peekForward()) { (isWhitespace(this) || this == '\n') && !isNextEOF()}) moveForward()
        if(!endOnWhitespace) moveForward()
        return !(if(!endOnWhitespace) isEOF() else isNextEOF())
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