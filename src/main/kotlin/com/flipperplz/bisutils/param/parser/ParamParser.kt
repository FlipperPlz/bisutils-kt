package com.flipperplz.bisutils.param.parser

import com.flipperplz.bisutils.param.ParamFile
import com.flipperplz.bisutils.param.ast.node.ParamNamedElement
import com.flipperplz.bisutils.param.ast.node.ParamNumerical
import com.flipperplz.bisutils.param.ast.statement.ParamClass
import com.flipperplz.bisutils.param.lexer.ParamLexer
import com.flipperplz.bisutils.param.utils.ParamOperatorTypes
import com.flipperplz.bisutils.param.utils.extensions.*
import com.flipperplz.bisutils.param.utils.mutability.*
import com.flipperplz.bisutils.param.utils.mutability.node.ParamMutableStatementHolder
import com.flipperplz.bisutils.parsing.LexerException
import com.flipperplz.bisutils.parsing.LexicalError
import com.flipperplz.bisutils.preprocesser.boost.BoostPreprocessor
import java.util.Stack

object ParamParser {

    @Throws(LexerException::class)
    fun parse(lexer: ParamLexer, name: String, preProcessor: BoostPreprocessor? = null): ParamFile = mutableParamFile(name).apply {
        preProcessor?.processText(lexer)
        val contextStack = Stack<ParamMutableStatementHolder>().also { it.add(this) }
        while (contextStack.isNotEmpty() ) {
            val currentContext = contextStack.peek()
            println("Loop started on context ${(currentContext as ParamNamedElement).slimName}")
            lexer.moveForward()
            lexer.traverseWhitespace(true)
            fun tryEnd(): Boolean {
                println("EOF ON LOOP START (${currentContext.getParamElementType()})")
                if(contextStack.count() != 1) throw lexer.eofException()
                return true
            }
            when {
                lexer.isEOF() -> if(tryEnd()) break
                lexer.currentChar == '#' -> throw LexerException(lexer, LexicalError.PreprocessorError)
                lexer.currentChar == '}' -> {

                    lexer.moveForward(); lexer.traverseWhitespace(false)
                    if(lexer.currentChar != ';') throw lexer.unexpectedInputException()
                    print("Found '};' Popping context. ${(contextStack.pop() as ParamNamedElement).slimName} ->")

                    println((contextStack.peek() as ParamNamedElement).slimName)
                    continue
                }
            }

            var keyword: String = lexer.readIdentifier(true)
            if(keyword.isBlank() && tryEnd()) break
            println("Loop KEYWORD IS '$keyword'")
            when(keyword) {
                "delete" -> {
                    if(lexer.traverseWhitespace() <= 0) throw lexer.unexpectedInputException()
                    keyword = lexer.readIdentifier(); lexer.traverseWhitespace()
                    if(lexer.currentChar != ';') throw lexer.unexpectedInputException()
                    currentContext += ParamMutableDeleteStatementImpl(slimName = keyword, slimParent = currentContext, containingParamFile = this)
                    continue
                }
                "class" -> {
                    if(lexer.traverseWhitespace() <= 0) throw lexer.unexpectedInputException()
                    keyword = lexer.readIdentifier(); lexer.traverseWhitespace()
                    var baseClass: String?
                    when(lexer.currentChar) {
                        ';' -> {
                            currentContext += ParamMutableExternalClassImpl(currentContext, this, keyword)
                            continue;
                        }
                        ':' -> {
                            lexer.moveForward(); lexer.traverseWhitespace(); baseClass = lexer.readIdentifier()
                            lexer.traverseWhitespace()
                        }
                        '{' -> { baseClass = null }
                        else -> throw lexer.unexpectedInputException()
                    }
                    if(lexer.currentChar != '{') throw lexer.unexpectedInputException()
                    println("Found class $keyword, adding to context after ${(currentContext as ParamNamedElement).slimName}")
                    with(ParamMutableClassImpl(currentContext, this, keyword, baseClass, mutableListOf())) {
                        currentContext += this
                        contextStack.push(this)
                    }
                    continue
                }
                "enum" -> {
                    if(lexer.traverseWhitespace() <= 0 && lexer.readIdentifier().isNotBlank()) throw lexer.unexpectedInputException()
                    var enumNum = 0
                    val enum = ParamMutableEnum(slimParent = currentContext, enumValues = mutableMapOf())
                    if(lexer.currentChar != '{') throw lexer.unexpectedInputException()
                    lexer.moveForward(); lexer.traverseWhitespace()
                    do {
                        keyword = lexer.readIdentifier()
                        lexer.traverseWhitespace(); lexer.moveForward()
                        val enumValue = if(lexer.previousChar != '=') enum.floatOf { enumNum.toFloat() } else
                            (lexer.readLiteral(enum, this, '}', ',') as? ParamNumerical) ?: throw lexer.unexpectedInputException()
                        enum.enumValues[keyword] = enumValue
                        enumNum++
                    } while (lexer.currentChar != '}')
                    lexer.traverseWhitespace()
                    if(lexer.currentChar != ';') throw lexer.unexpectedInputException()
                    currentContext += enum
                }
                "__EXEC" -> {
                    lexer.traverseWhitespace()
                    if(lexer.currentChar != '(') throw lexer.unexpectedInputException()

                    var i = 1
                    val builder = StringBuilder()
                    while (!(lexer.moveForward() == ')' && i > 1)) {
                        if(lexer.currentChar == ')') i++
                        if(lexer.currentChar == '(') i--
                        builder.append(lexer.currentChar)
                    }
                    //skip while whitespace or ;
                    continue
                }
                else -> {
                    lexer.traverseWhitespace()
                    if (lexer.currentChar == '[') { lexer.moveForward(); lexer.traverseWhitespace()
                        if (lexer.currentChar != ']') { throw lexer.unexpectedInputException() };
                        lexer.moveForward(); lexer.traverseWhitespace()
                        val operator: ParamOperatorTypes = lexer.readOperator(); lexer.moveForward(); lexer.traverseWhitespace()
                        println("Enter variable $keyword[]")
                        currentContext.slimCommands.add(ParamMutableVariableStatementImpl(currentContext, this, keyword).also {
                            it.slimValue = lexer.readArray(this, this)
                            it.slimOperator = operator
                        })
                        while (lexer.currentChar == ';') lexer.moveForward()
                        continue
                    } else if (lexer.currentChar == '=') {
                        println("Enter variable $keyword")
                        lexer.moveForward(); lexer.traverseWhitespace()
                        currentContext.slimCommands.add(ParamMutableVariableStatementImpl(currentContext, this, keyword).also {
                            it.slimValue = lexer.readLiteral(this, this, ';')
                        })
                        lexer.traverseWhitespace()
                        if (lexer.currentChar != ';') throw lexer.unexpectedInputException()
                        lexer.moveForward()
                        continue
                    }
                    println("Unknown $keyword")

                    throw lexer.unexpectedInputException()
                }
            }
        }
    }
}
