package com.flipperplz.bisutils.param.parser

import com.flipperplz.bisutils.param.ParamFile
import com.flipperplz.bisutils.param.ast.node.ParamNumerical
import com.flipperplz.bisutils.param.lexer.ParamLexer
import com.flipperplz.bisutils.param.utils.ParamOperatorTypes
import com.flipperplz.bisutils.param.utils.extensions.*
import com.flipperplz.bisutils.param.utils.mutability.*
import com.flipperplz.bisutils.param.utils.mutability.node.ParamMutableStatementHolder
import com.flipperplz.bisutils.parsing.LexerException
import com.flipperplz.bisutils.parsing.LexicalError
import java.util.Stack

object ParamParser {
    fun parse(lexer: ParamLexer, name: String, preProcessor: ParamPreProcessor? = null): ParamFile = mutableParamFile(name).apply {
        val finalLexer = preProcessor?.processText(lexer) ?: lexer
        val contextStack = Stack<ParamMutableStatementHolder>().also { it.add(this) }
        while (contextStack.isNotEmpty()) {
            val currentContext = contextStack.peek()
            finalLexer.moveForward()
            finalLexer.traverseWhitespace(true)
            when {
                finalLexer.isEOF() -> {
                    if(contextStack.count() != 1) throw finalLexer.eofException()
                    break
                }
                finalLexer.currentChar == '#' -> throw LexerException(finalLexer, LexicalError.PreprocessorError)
                finalLexer.currentChar == '}' -> {
                    finalLexer.moveForward(); finalLexer.traverseWhitespace(false)
                    if(finalLexer.currentChar != ';') throw finalLexer.unexpectedInputException()
                    contextStack.pop(); continue
                }
            }
            var keyword: String = finalLexer.readIdentifier()
            when(keyword) {
                "delete" -> {
                    if(finalLexer.traverseWhitespace() <= 0) throw finalLexer.unexpectedInputException()
                    keyword = finalLexer.readIdentifier(); finalLexer.traverseWhitespace()
                    if(finalLexer.currentChar != ';') throw finalLexer.unexpectedInputException()
                    currentContext += ParamMutableDeleteStatementImpl(slimName = keyword, slimParent = currentContext, containingParamFile = this)
                    continue
                }
                "class" -> {
                    if(finalLexer.traverseWhitespace() <= 0) throw finalLexer.unexpectedInputException()
                    keyword = finalLexer.readIdentifier(); finalLexer.traverseWhitespace()
                    var baseClass: String?
                    when(finalLexer.currentChar) {
                        ';' -> {
                            currentContext += ParamMutableExternalClassImpl(currentContext, this, keyword)
                            continue;
                        }
                        ':' -> {
                            finalLexer.moveForward(); finalLexer.traverseWhitespace(); baseClass = finalLexer.readIdentifier()
                            finalLexer.traverseWhitespace()
                        }
                        '{' -> { baseClass = null }
                        else -> throw finalLexer.unexpectedInputException()
                    }
                    if(finalLexer.currentChar != '{') throw finalLexer.unexpectedInputException()
                    with(ParamMutableClassImpl(currentContext, this, keyword, baseClass, mutableListOf())) {
                        currentContext += this
                        contextStack.push(this)
                    }
                    continue
                }
                "enum" -> {
                    if(finalLexer.traverseWhitespace() <= 0 && finalLexer.readIdentifier().isNotBlank()) throw finalLexer.unexpectedInputException()
                    var enumNum = 0
                    val enum = ParamMutableEnum(slimParent = currentContext, enumValues = mutableMapOf())
                    if(finalLexer.currentChar != '{') throw finalLexer.unexpectedInputException()
                    finalLexer.moveForward(); finalLexer.traverseWhitespace()
                    do {
                        keyword = finalLexer.readIdentifier()
                        finalLexer.traverseWhitespace(); finalLexer.moveForward()
                        val enumValue = if(finalLexer.previousChar != '=') enum.floatOf { enumNum.toFloat() } else
                            (finalLexer.readLiteral(enum, this, '}', ',') as? ParamNumerical) ?: throw finalLexer.unexpectedInputException()
                        enum.enumValues[keyword] = enumValue
                        enumNum++
                    } while (finalLexer.currentChar != '}')
                    finalLexer.traverseWhitespace()
                    if(finalLexer.currentChar != ';') throw finalLexer.unexpectedInputException()
                    currentContext += enum
                }
                "__EXEC" -> {
                    finalLexer.traverseWhitespace()
                    if(finalLexer.currentChar != '(') throw finalLexer.unexpectedInputException()

                    var i = 1
                    val builder = StringBuilder()
                    while (!(finalLexer.moveForward() == ')' && i > 1)) {
                        if(finalLexer.currentChar == ')') i++
                        if(finalLexer.currentChar == '(') i--
                        builder.append(finalLexer.currentChar)
                    }
                    //skip while whitespace or ;
                    continue
                }
                else -> {
                    if (finalLexer.currentChar == '[') { finalLexer.moveForward(); finalLexer.traverseWhitespace()
                        if (finalLexer.currentChar != ']') { throw finalLexer.unexpectedInputException() }; finalLexer.traverseWhitespace()
                        val operator: ParamOperatorTypes = finalLexer.readOperator(); finalLexer.moveForward(); finalLexer.traverseWhitespace()
                        currentContext.slimCommands.add(ParamMutableVariableStatementImpl(currentContext, this, keyword).also {
                            it.slimValue = finalLexer.readArray(this, this)
                            it.slimOperator = operator
                        })
                        while (finalLexer.currentChar == ';') finalLexer.moveForward()
                        continue
                    } else if (finalLexer.currentChar == '=') {
                        currentContext.slimCommands.add(
                            ParamMutableVariableStatementImpl(currentContext, this, keyword).also {
                                it.slimValue = finalLexer.readLiteral(this, this, ';')
                            }
                        )
                        finalLexer.traverseWhitespace()
                        if (finalLexer.currentChar != ';') throw finalLexer.unexpectedInputException()
                        continue
                    }
                    throw finalLexer.unexpectedInputException()
                }
            }
        }
    }
}
