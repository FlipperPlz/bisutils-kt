package com.flipperplz.bisutils.preprocesser.boost

import com.flipperplz.bisutils.BisPreProcessor
import com.flipperplz.bisutils.parsing.BisLexer
import com.flipperplz.bisutils.parsing.LexerException
import com.flipperplz.bisutils.parsing.LexicalError
import com.flipperplz.bisutils.preprocesser.boost.utils.BoostDirective
import com.flipperplz.bisutils.preprocesser.boost.utils.BoostDirectiveType
import java.lang.StringBuilder

class BoostPreprocessor(
       val locateFile: (String) -> String? = { "class MyMod {};" }
) : BisPreProcessor<BisLexer> {
    companion object {
        val whitespaces: List<Char> = mutableListOf(' ', '\t', '\u000B', '\u000C')

        fun preprocessorException(lexer: BisLexer) = LexerException(lexer, LexicalError.PreprocessorError)

        @Throws(LexerException::class)
        fun traverseWhitespace(lexer: BisLexer, allowEOF: Boolean = false): Int {
            with(lexer) {
                var count: Int = 0
                while (true) {
                    if (isEOF()) {
                        if (allowEOF) break
                        else throw eofException()
                    }
                    when (currentChar) {
                        '\r' -> { count++; if (moveForward() != '\n') continue }
                        '\n' -> { count++; moveForward() }
                        else -> {
                            if (!whitespaces.contains(currentChar)) break else {
                                moveForward().also { count++ }
                                val extra = traverseComments(lexer)
                                if(extra == 0) break
                                count += extra
                            }
                        }
                    }
                }
                return count;
            }
        }

        fun traverseComments(lexer: BisLexer): Int {
            val instructionStart = lexer.bufferPtr
            if(lexer.currentChar == '/') when(lexer.moveForward()) {
                '/' -> { //------Line Comment------
                    val lineEnd = lexer.traverseLine() + instructionStart
                    lexer.removeRange(instructionStart..lineEnd)
                    lexer.jumpTo(instructionStart);
                    return lineEnd - instructionStart
                }
                '*' -> { //------Block Comment------
                    var length = 2
                    while (!lexer.isEOF() && !(lexer.previousChar=='*' && lexer.currentChar=='/'))
                        lexer.moveForward().also { length++ }
                    lexer.removeRange(instructionStart..length)
                    lexer.jumpTo(instructionStart);
                    return length
                }
            }
            return 0
        }
        private fun BisLexer.traverseLine(): Int {
            var count: Int = 0
            while(true){
                if(isEOF()) break
                when(currentChar) {
                    '\r' -> { count++; if(moveForward() == '\n') { count++; moveForward()}; break }
                    '\n' -> { count++; moveForward(); break }
                    else -> { moveForward(); count++; continue }
                }
            }
            return count;
        }

    }

    @Throws(LexerException::class)
    override fun processUntil(lexer: BisLexer) {
        //lexer.replaceAll("\r\n", "\n")
        lexer.replaceInLine(Regex("""//.*(\n|\r\n)"""), "")
        lexer.replaceInLine(Regex("""/\\*/"""), "")
        lexer.replaceInLine(Regex("""/\\*.*\\*/"""), "")
        lexer.replaceInLine("__LINE__", "-1")
        lexer.replaceInLine("__FILE__", "config.cpp")
        lexer.resetPosition()
    }
    @Throws(LexerException::class)
    private fun processMacros(lexer: BisLexer) {}

    @Throws(LexerException::class)
    fun processDirective(slimName: String?, lexer: BisLexer): BoostDirective {
        val start = lexer.bufferPtr - 1
        val keyword = StringBuilder().apply {
            while(lexer.moveForward()?.isWhitespace() != true) append(lexer.currentChar)
        }.toString()
        return (BoostDirectiveType.directiveForKeyword(keyword) ?: throw preprocessorException(lexer)).parse(lexer, this).apply {
            lexer.replaceRange(start..lexer.bufferPtr+1, process())
        }
    }


}