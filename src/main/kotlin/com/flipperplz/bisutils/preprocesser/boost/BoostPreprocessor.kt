package com.flipperplz.bisutils.preprocesser.boost

import com.flipperplz.bisutils.BisPreProcessor
import com.flipperplz.bisutils.parsing.BisLexer
import com.flipperplz.bisutils.parsing.LexerException
import com.flipperplz.bisutils.parsing.LexicalError
import com.flipperplz.bisutils.preprocesser.boost.directive.BoostDefineDirective
import com.flipperplz.bisutils.preprocesser.boost.utils.BoostDirective
import com.flipperplz.bisutils.preprocesser.boost.utils.BoostDirectiveType
import java.lang.StringBuilder

typealias DefineDirective = BoostDefineDirective
class BoostPreprocessor(
       private val _defines: MutableList<DefineDirective>  = mutableListOf(),
       val locateFile: (String) -> String? = { "class MyMod {};" }
) : BisPreProcessor<BisLexer> {
    companion object {
        val whitespaces: List<Char> = mutableListOf(' ', '\t', '\u000B', '\u000C')

        fun preprocessorException(lexer: BisLexer) = LexerException(lexer, LexicalError.PreprocessorError)

        fun readMacroID(lexer: BisLexer, throwOnNone: Boolean = false): String {
            if(with(lexer.currentChar) {this != null && isLetter() || this == '_' }) {
                return StringBuilder().apply {
                    append(lexer.currentChar)
                    while (with(lexer.moveForward()) { this != null && (this == '_' || this.isLetterOrDigit()) })
                        append(lexer.currentChar)
                }.toString()
            }
            if(throwOnNone) throw preprocessorException(lexer)
            return ""
        }

        @Throws(LexerException::class)
        fun traverseWhitespace(lexer: BisLexer, allowEOF: Boolean = false, allowEOL: Boolean = true, allowDirectiveEOL: Boolean = true): Int {
            with(lexer) {
                var count: Int = 0
                while (true) {
                    if (isEOF()) {
                        if (allowEOF) break
                        else throw eofException()
                    }
                    when (currentChar) {
                        '\\' -> {
                            if(!allowDirectiveEOL) break
                            val next =peekForward()
                            if(next != '\r' && next != '\n') break
                            count++
                        }
                        '\r' -> {
                            if(!allowEOL) throw lexer.eolException();
                            count++;
                            if (moveForward() != '\n') continue
                            count++;
                        }
                        '\n' -> {
                            if(!allowEOL) throw lexer.eolException();
                            count++; moveForward()
                        }
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
    val defines: List<DefineDirective>
        get() = _defines


    fun undefine(macroName: String) = _defines.removeIf { it.macroName == macroName }

    fun define(macro: DefineDirective) =
        if(_defines.firstOrNull { it.macroName == macro.macroName } != null)
            throw Exception("A macro with name '${macro.macroName}' already exists.")
        else _defines.add(macro)

    fun locateMacro(name: String): DefineDirective? = _defines.firstOrNull { it.macroName == name }

    @Throws(LexerException::class)
    override fun processLine(lexer: BisLexer) {
        //lexer.replaceAll("\r\n", "\n")
        lexer.replaceInLine(Regex("""/\\*/"""), "")
        lexer.replaceInLine(Regex("""/\\*.*\\*/"""), "")
        lexer.replaceInLine("__LINE__", "-1")
        lexer.replaceInLine("__FILE__", "config.cpp")
        lexer.resetPosition()
    }

    @Throws(LexerException::class)
    private fun processMacros(lexer: BisLexer) {}

    @Throws(LexerException::class)
    fun processDirective(lexer: BisLexer): BoostDirective {
        val start = lexer.bufferPtr - 1
        val keyword = StringBuilder().apply {
            while(lexer.moveForward()?.isWhitespace() != true) append(lexer.currentChar)
        }.toString()
        return (BoostDirectiveType.directiveForKeyword(keyword) ?: throw preprocessorException(lexer)).parse(lexer, this).apply {
            lexer.replaceRange(start..lexer.bufferPtr+1, process())
        }
    }


}