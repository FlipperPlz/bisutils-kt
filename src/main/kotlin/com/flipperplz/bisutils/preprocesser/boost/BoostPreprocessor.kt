package com.flipperplz.bisutils.preprocesser.boost

import com.flipperplz.bisutils.preprocesser.BisPreprocessor
import com.flipperplz.bisutils.parsing.BisLexer
import com.flipperplz.bisutils.parsing.LexerException
import com.flipperplz.bisutils.parsing.LexicalError
import com.flipperplz.bisutils.preprocesser.boost.ast.*
import com.flipperplz.bisutils.preprocesser.boost.ast.directive.*
import com.flipperplz.bisutils.preprocesser.boost.ast.impl.element.BoostMacroElementImpl
import com.flipperplz.bisutils.preprocesser.boost.utils.BoostDirectiveType
import com.flipperplz.bisutils.preprocesser.boost.utils.BoostIncludeNotFoundException

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
            BoostMacroElementImpl(lexer, this).process(this)?.let {
                lexer.replaceRange(start..lexer.bufferPtr, it)
            }
            lexer.moveForward()
        }
    }

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
                            if(!allowEOL) throw lexer.eolException()
                            count++
                            if (moveForward() != '\n') continue
                            count++
                        }
                        '\n' -> {
                            if(!allowEOL) throw lexer.eolException()
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
                return count
            }
        }

        fun traverseComments(lexer: BisLexer): Int {
            val instructionStart = lexer.bufferPtr
            if(lexer.currentChar == '/') when(lexer.moveForward()) {
                '/' -> { //------Line Comment------
                    val lineEnd = lexer.traverseLine() + instructionStart
                    lexer.removeRange(instructionStart..lineEnd)
                    lexer.jumpTo(instructionStart)
                    return lineEnd - instructionStart
                }
                '*' -> { //------Block Comment------
                    var length = 2
                    while (!lexer.isEOF() && !(lexer.previousChar=='*' && lexer.currentChar=='/'))
                        lexer.moveForward().also { length++ }
                    lexer.removeRange(instructionStart..length)
                    lexer.jumpTo(instructionStart)
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
            return count
        }

    }


}