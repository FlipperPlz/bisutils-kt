package com.flipperplz.bisutils.param.parser

import com.flipperplz.bisutils.BisPreProcessor
import com.flipperplz.bisutils.param.ParamFile
import com.flipperplz.bisutils.param.lexer.ParamLexer
import com.flipperplz.bisutils.param.utils.mutability.*
import com.flipperplz.bisutils.param.utils.mutability.node.ParamMutableStatementHolder
import com.flipperplz.bisutils.parsing.BisLexer
import java.util.Stack
import kotlin.math.ceil
import kotlin.math.floor

class ParamParser(lexer: ParamLexer, name: String, preProcessor: BisPreProcessor? = null) {

    fun parse(): ParamFile {
        TODO()
    }

//
//    fun readParam(name: String, lexer: BisLexer, preProcessor: BisPreProcessor?): ParamFile {
//        if(preProcessor?.processText(lexer) == false) throw Exception("Preprocess failed.")
//
//        val file = ParamMutableFile(name)
//        val contextStack = Stack<ParamMutableStatementHolder>().apply { push(file) }
//        var currentContext: ParamMutableStatementHolder
//    with(lexer) {
//        while (contextStack.isNotEmpty()) {
//            currentContext = contextStack.peek()
//
//            val c = moveForward()
//            when {
//                isWhitespace() || isEOL() -> continue
//                isEOF() -> { contextStack.pop(); continue; }
//                c == '#' -> throw ParamParseException(lexer, ParamParseError.UnexpectedInput)
//                c == '}' -> {
//                    moveForward()
//                    while (isWhitespace() && !isEOF()) moveForward()
//                    if(isEOF()) throw ParamParseException(lexer, ParamParseError.PrematureFileEnd, null, null)
//                    if(currentChar != ';') throw ParamParseException(lexer, ParamParseError.UnexpectedInput, null, null, listOf(";"))
//                    contextStack.pop()
//                    continue
//                }
//                else -> moveBackward()
//            }
//
//            when (val keyword = readIdentifier()) {
//                "delete" -> {
//                    val context = readIdentifier()
//                    if(!traverseWhitespace(false))
//                        throw ParamParseException(lexer, ParamParseError.PrematureFileEnd, "delete", null, listOf("identifier"))
//                    if(currentChar != ';') throw ParamParseException(lexer, ParamParseError.UnexpectedInput, "delete", null, listOf(";"))
//                    val delete = ParamMutableDeleteStatement(slimName = context, slimParent = currentContext, containingParamFile = file)
//                    currentContext.slimCommands.add(delete as ParamDeleteStatement)
//                    continue
//                }
//                "class" -> {
//                    val className = readIdentifier()
//                    var base = ""
//                    if(!traverseWhitespace(false))
//                        throw ParamParseException(lexer, ParamParseError.PrematureFileEnd, "class", null, listOf(":", ";", "{"))
//
//                    when(currentChar) {
//                        ';' -> {
//                            val external = ParamMutableExternalClass(slimParent = currentContext, slimName = className)
//                            currentContext.slimCommands.add(external as ParamExternalClass)
//                            continue
//                        }
//                        ':' -> {
//                            moveForward()
//                            base = readIdentifier()
//                            if(!traverseWhitespace(false))
//                                throw ParamParseException(lexer, ParamParseError.PrematureFileEnd, "class", null, listOf(":", ";", "{"))
//                            if(lexer.currentChar != '{')
//                                throw ParamParseException(lexer, ParamParseError.UnexpectedInput, "class", null, listOf("{"))
//                        }
//                        '{' -> { }
//                        else -> throw ParamParseException(lexer, ParamParseError.UnexpectedInput, "class", null, listOf(":", ";", "{"))
//                    }
//                    contextStack.push(ParamMutableClass(currentContext, file, className, base, mutableListOf())).also {
//                        currentContext.slimCommands.add(it as ParamClass)
//                    }
//                }
//                "enum" -> {
//                    readIdentifier()
//                    if(!traverseWhitespace(false))
//                        throw ParamParseException(lexer, ParamParseError.PrematureFileEnd, "enum", null, listOf("{"))
//                    if(currentChar != '{') throw ParamParseException(lexer, ParamParseError.UnexpectedInput, "enum", null, listOf("{"))
//
//                    var enumValue = 0
//                    val enum = ParamMutableEnum(slimParent = currentContext, enumValues = mutableMapOf())
//                    do {
//                        val enumName = readIdentifier()
//                        if(!traverseWhitespace(false))
//                            throw ParamParseException(lexer, ParamParseError.PrematureFileEnd, "enum", null, listOf(",", "}", "="))
//                        if(currentChar != '=') Unit //TODO: Set Enum Value
//                        enum.enumValues[enumName] = enumValue
//                        enumValue++
//                        if(!traverseWhitespace(false))
//                            throw ParamParseException(lexer, ParamParseError.PrematureFileEnd, "enum", null, listOf(",", "}"))
//                    } while (currentChar == ',')
//                    currentContext.slimCommands.add(enum as ParamEnum)
//                }
//                "__EXEC" -> {
//                    if(!traverseWhitespace(false))
//                        throw Exception("${lexer.getPositionstring()} Error: Premature end of file, expected '('.")
//                    if(currentChar != '(') throw Exception("${getPositionstring()} Error: expected '(' instead got '$currentChar'.")
//                    @Suppress("UNUSED_VARIABLE") //TODO(Ryann): Encapsulate parser and add abstraction for validating SQF
//                    val expression = getUntil { it.currentChar == ')' }.trimEnd(')', ' ')
//
//                    skipWhile { currentChar == ';' || isWhitespace() }
//                    continue
//                }
//                else ->  {
//                    if(currentChar == '[') {
//                        moveForward()
//                        if((isWhitespace() || isEOL()) && !traverseWhitespace(false))
//                            throw ParamParseException(lexer, ParamParseError.PrematureFileEnd, "array_variable", null, listOf("]"))
//                        if(currentChar != ']')
//                            throw ParamParseException(lexer, ParamParseError.UnexpectedInput, "array_variable", null, listOf("]"))
//                        if(!traverseWhitespace(true))
//                            throw ParamParseException(lexer, ParamParseError.PrematureFileEnd, "array_variable", null, listOf("=", "+=", "-="))
//
//                        val operator: ParamOperatorTypes = when(val text = getWhile { with(peekForward()) {this == '+' || this == '-' || this == '='} }) {
//                            "=" -> ParamOperatorTypes.ASSIGN
//                            "+=" -> ParamOperatorTypes.ADD_ASSIGN
//                            "-=" -> ParamOperatorTypes.SUB_ASSIGN
//                            else -> throw ParamParseException(lexer, ParamParseError.UnexpectedInput, "array_variable", "Found $text.", listOf("=", "+=", "-="))
//                        }
//                        currentContext.slimCommands.add(ParamMutableVariableStatement(currentContext, file, keyword).apply {
//                            slimValue = readArray(lexer, this, file)
//                            slimOperator = operator
//                        } as ParamVariableStatement)
//                        skipWhile { currentChar == ';' || isWhitespace() }
//
//                    } else if(currentChar == '=') {
//                        currentContext.slimCommands.add(ParamMutableVariableStatement(currentContext, file, keyword).apply {
//                            slimValue = readLiteral(lexer, this, file, ';')
//                        })
//                        traverseWhitespace(false)
//                        if(currentChar != ';') throw unexpectedInputException()
//
//                        continue
//                    } else throw ParamParseException(lexer, ParamParseError.UnexpectedInput, "variable", null, listOf("=", "["))
//                    continue
//                }
//            }
//        }
//    }
//
//        return file
//    }




}
