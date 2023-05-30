package com.flipperplz.bisutils.preprocesser.boost

import com.flipperplz.bisutils.preprocesser.BisPreprocessor
import com.flipperplz.bisutils.parsing.BisLexer
import com.flipperplz.bisutils.parsing.LexerException
import com.flipperplz.bisutils.parsing.LexicalError
import com.flipperplz.bisutils.preprocesser.boost.directive.*
import com.flipperplz.bisutils.preprocesser.boost.utils.BoostDirective
import com.flipperplz.bisutils.preprocesser.boost.utils.BoostDirectiveType
import com.flipperplz.bisutils.preprocesser.boost.utils.BoostIncludeNotFoundException
import java.lang.StringBuilder

typealias DefineDirective = BoostDefineDirective
class BoostPreprocessor(
       private val _defines: MutableList<DefineDirective> = mutableListOf(),
       val locateFile: (String) -> String? = { "class RandomDirective {};" }
) : BisPreprocessor {
    var defines: List<DefineDirective>
        get() = _defines
        set(value) {
            _defines.clear()
            _defines.addAll(value)
        }

    fun undefine(macroName: String) = _defines.removeIf { it.macroName == macroName }


    fun locateMacro(name: String): DefineDirective? = _defines.firstOrNull { it.macroName == name }

    @Throws(LexerException::class)
    override fun processLexer(lexer: BisLexer) {
        var quoted = false

        while (!lexer.isEOF()) {
            val start = lexer.bufferPtr
            if(lexer.currentChar == '"') quoted = !quoted
            if(quoted) { lexer.moveForward(); continue; }
            if(lexer.currentChar == '/' && traverseComments(lexer) == 0) continue
            if(lexer.currentChar == '#') {
                val directive = BoostDirectiveType.parse(lexer, this) ?: throw preprocessorException(lexer)
                lexer.replaceRange(start..lexer.bufferPtr, processDirective(directive))
                lexer.jumpTo(start+1)
                continue
            }
            val macro = readMacroID(lexer)
            when {
                macro.isEmpty() -> { lexer.moveForward(); continue }
                macro == "__LINE__" -> lexer.replaceRange(start..lexer.bufferPtr, "111")//TODO: LINE COUNT
                macro == "__FILE__" -> lexer.replaceRange(start..lexer.bufferPtr, "filename")//TODO: LINE COUNT
                macro == "__EXEC" -> throw Exception()//unsupported; read to closing ')' and fuck off
                macro == "__EVAL" -> throw Exception()
                else -> locateMacro(macro)?.let {
                    lexer.replaceRange(start..lexer.bufferPtr,  processMacro(it))
                }
            }
        }
    }

    @Throws(LexerException::class)
    private fun processMacro(macro: BoostDefineDirective): String = ""


    @Throws(LexerException::class, BoostIncludeNotFoundException::class)
    fun processDirective(directive: BoostDirective): String = when(directive) {
        is BoostDefineDirective -> "".also { _defines.add(directive) }
        is BoostIfNDefinedDirective -> directive.evaluate()
        is BoostIfDefinedDirective -> directive.evaluate()
        is BoostIncludeDirective -> processInclude(directive)
        is BoostUndefineDirective -> "".also { undefine(directive.macroName) }
        else -> "Error Processing Directive (${directive.getType()})"
    }

    @Throws(BoostIncludeNotFoundException::class)
    fun processInclude(include: BoostIncludeDirective): String = ""

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
                        if (allowEOF) break else throw eofException()
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


}