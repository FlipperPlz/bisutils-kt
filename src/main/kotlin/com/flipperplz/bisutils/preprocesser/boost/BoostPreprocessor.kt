package com.flipperplz.bisutils.preprocesser.boost

import com.flipperplz.bisutils.BisPreProcessor
import com.flipperplz.bisutils.parsing.BisLexer
import com.flipperplz.bisutils.parsing.LexerException

class BoostPreprocessor : BisPreProcessor<BisLexer> {
    companion object {
        val whitespaces: List<Char> = mutableListOf(' ', '\t', '\u000B', '\u000C')
    }

    @Throws(LexerException::class)
    private fun BisLexer.traverseWhitespace(allowEOF: Boolean = false): Int {
        var count: Int = 0
        while(true){
            if(isEOF()) {
                if(allowEOF) break
                else throw eofException()
            }
            when(currentChar) {
                '\r' -> { count++; if(moveForward() != '\n') continue  }
                '\n' -> { count++; moveForward() }
                else -> { if(!whitespaces.contains(currentChar)) break else moveForward().also { count++ }; }
            }
        }
        return count;
    }
    private fun BisLexer.traverseLine(): Int {
        var count: Int = 0
        while(true){
            if(isEOF()) break
            when(currentChar) {
                '\r' -> { count++; if(moveForward() == '\n') { count++; moveForward() }; break}
                '\n' -> { count++; moveForward(); break }
                else -> { count++; continue }
            }
        }
        return count;
    }

    override fun processText(lexer: BisLexer) {
        val startPosition = lexer.bufferPtr
        lexer.resetPosition()

        while (!lexer.isEOF()) {
            val currentPtr = lexer.bufferPtr
            when(lexer.currentChar) {
                '/' -> {
                    if(lexer.moveForward() == '/') {
                        lexer.removeRange(currentPtr..lexer.traverseLine())
                        lexer.jumpTo(currentPtr);
                    }
                    continue
                }
                '\\' -> {
                    lexer.moveForward()
                    if(lexer.currentChar == '\n') lexer.removeRange(currentPtr..lexer.bufferPtr)
                    else if(lexer.currentChar == '\r') {
                        if(lexer.peekForward() == '\n') lexer.moveForward()
                        lexer.removeRange(currentPtr..lexer.bufferPtr)
                    }
                    continue
                }
            }
        }
    }

}