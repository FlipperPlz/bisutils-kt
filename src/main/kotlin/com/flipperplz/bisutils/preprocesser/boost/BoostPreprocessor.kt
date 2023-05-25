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
                else -> { if(!whitespaces.contains(currentChar)) break else moveForward().also { count++ } }
            }
        }
        return count;
    }

    private fun BisLexer.traverseLine(): Int {
        var count: Int = 0
        while(true){
            if(isEOF()) break
            when(currentChar) {
                '\r' -> { count++; if(moveForward() == '\n') { count++; moveForward()}; break }
                '\n' -> { count++; moveForward(); break }
                else -> { count++; continue }
            }
        }
        return count;
    }

    @Throws(LexerException::class)
    override fun processText(lexer: BisLexer) {
        //lexer.replaceAll("\r\n", "\n")
        run {
            lexer.replaceAll("\\\n", "")
            lexer.replaceAll("\\\r\n", "")
            processMacros(lexer)
        }

        lexer.resetPosition()
        while (!lexer.isEOF()) {
            val instructionStart = lexer.bufferPtr + lexer.traverseWhitespace()
            when(lexer.currentChar) {
                '/' -> /*------Comments------*/ {
                    when(lexer.moveForward()) {
                        '/' -> { //------Line Comment------
                            lexer.removeRange(instructionStart..lexer.traverseLine())
                            lexer.jumpTo(instructionStart);
                        }
                        '*' -> { //------Block Comment------
                            while (!lexer.isEOF() && !(lexer.previousChar=='*' && lexer.currentChar=='/'))
                                lexer.moveForward()
                            lexer.removeRange(instructionStart..lexer.traverseLine())
                            lexer.jumpTo(instructionStart);
                        }
                    }
                }
                '_' -> /*------Macros------*/ {
                    if(lexer.moveForward() != '_') continue
                    val keyword = lexer.readChars(4)

                    if(lexer.moveForward() != '_' || lexer.moveForward() != '_' ) continue

                    when(keyword) {
                        "LINE" -> {
                            lexer.removeRange(instructionStart..lexer.traverseLine())
                            lexer.jumpTo(instructionStart); lexer.shoveText("\"-1\"")
                            continue
                        }
                        "FILE" -> {
                            lexer.removeRange(instructionStart..lexer.traverseLine())
                            lexer.jumpTo(instructionStart); lexer.shoveText("\"testFileName\"")
                            continue
                        }
                        else -> { lexer.moveBackward(5); continue }
                    }
                }
            }

        }
    }


    @Throws(LexerException::class)
    private fun processMacros(lexer: BisLexer) {}

    @Throws(LexerException::class)
    fun processDirective(slimName: String?, lexer: BisLexer) {}

}