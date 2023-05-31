package com.flipperplz.bisutils.preprocesser.enforce

import com.flipperplz.bisutils.preprocesser.BisPreprocessor
import com.flipperplz.bisutils.parsing.BisLexer
import com.flipperplz.bisutils.parsing.LexerException
import com.flipperplz.bisutils.preprocesser.boost.BoostPreprocessor
import com.flipperplz.bisutils.preprocesser.boost.ast.directive.*
import com.flipperplz.bisutils.preprocesser.boost.astImpl.element.BoostMacroElementImpl
import com.flipperplz.bisutils.preprocesser.boost.utils.BoostIncludeNotFoundException
import com.flipperplz.bisutils.preprocesser.enforce.ast.EnforceDirective
import com.flipperplz.bisutils.preprocesser.enforce.ast.directive.EnforceDefineDirective
import com.flipperplz.bisutils.preprocesser.enforce.ast.directive.EnforceIncludeDirective
import com.flipperplz.bisutils.preprocesser.enforce.astImpl.directive.EnforceIncludeDirectiveImpl
import com.flipperplz.bisutils.preprocesser.enforce.utils.EnforceIncludeNotFoundException

class EnforcePreprocessor(
    private val _defines: MutableList<EnforceDefineDirective> = mutableListOf(),
    val locateFile: (EnforceIncludeDirective) -> String? = { "" }
) : BisPreprocessor {
    var defines: List<EnforceDefineDirective>
        get() = _defines
        set(value) {
            _defines.clear()
            _defines.addAll(value)
        }


    override fun processLexer(lexer: BisLexer) {
        var quoted = false

        while (!lexer.isEOF()) {
            if(lexer.currentChar == '"') quoted = !quoted
            if(quoted) { lexer.moveForward(); continue; }
            val start = lexer.bufferPtr
            if((lexer.previousChar == null || lexer.previousChar == '\n') && lexer.currentChar == '#') {
                lexer.moveForward()
                val directive: EnforceDirective? = when(BoostMacroElementImpl.readMacroID(lexer)) {
                    "include" -> {
                        BoostPreprocessor.traverseWhitespace(lexer, allowEOF = false, allowEOL = false, allowDirectiveEOL = true)
                        if(lexer.currentChar != '"') throw lexer.unexpectedInputException()
                        val path = StringBuilder().apply {
                            lexer.moveForward()
                            while (!lexer.isEOF()) {
                                when(lexer.currentChar) {
                                    '\\' -> {
                                        when(val current = lexer.moveForward()) {
                                            '\r' -> if(lexer.moveForward() == '\n')
                                                append('\n') else
                                                throw lexer.unexpectedInputException()
                                            '\n' -> append('\n')
                                            '\"' -> append('"')
                                            else -> append('\\').append(current)
                                        }
                                        continue
                                    }
                                    '"' -> break
                                    else -> append(lexer.currentChar).also { lexer.moveForward() }
                                }

                            }
                        }.toString()
                        EnforceIncludeDirectiveImpl(this, path)
                    }
                    else -> null
                }

                directive?.let {
                    lexer.replaceRange(start..lexer.bufferPtr, processDirective(it))
                }
            }
            if(lexer.bufferPtr == start) lexer.moveForward()
        }
    }

    @Throws(LexerException::class, EnforceIncludeNotFoundException::class)
    fun processDirective(directive: EnforceDirective): String = when(directive) {
        is EnforceIncludeDirective -> processInclude(directive)
        else -> "Unknown enforce directive $directive"
    }

    @Throws(EnforceIncludeNotFoundException::class)
    fun processInclude(include: EnforceIncludeDirective): String =
        locateFile(include) ?: throw EnforceIncludeNotFoundException(include)


}