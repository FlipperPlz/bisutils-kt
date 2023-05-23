package com.flipperplz.bisutils.param.parser

import com.flipperplz.bisutils.BisPreProcessor
import com.flipperplz.bisutils.param.ParamFile
import com.flipperplz.bisutils.param.lexer.ParamLexer
import com.flipperplz.bisutils.param.utils.ParamElementTypes
import com.flipperplz.bisutils.param.utils.ParamOperatorTypes
import com.flipperplz.bisutils.param.utils.extensions.mutableParamFile
import com.flipperplz.bisutils.param.utils.extensions.plusAssign
import com.flipperplz.bisutils.param.utils.mutability.*
import com.flipperplz.bisutils.param.utils.mutability.node.ParamMutableStatementHolder
import com.flipperplz.bisutils.parsing.BisLexer
import com.flipperplz.bisutils.parsing.LexerException
import com.flipperplz.bisutils.parsing.LexicalError
import java.util.Stack
import kotlin.math.ceil
import kotlin.math.floor


class ParamParser(lexer: ParamLexer, name: String, preProcessor: BisPreProcessor? = null) : ParamMutableFile by mutableParamFile(name) {
    init {
        if(preProcessor?.processText(lexer) == false) throw LexerException(lexer, LexicalError.PreprocessorError)
        val contextStack = Stack<ParamMutableStatementHolder>().apply { add(this@ParamParser) }
        while (contextStack.isNotEmpty()) {
            val currentContext = contextStack.peek()
            lexer.moveForward()
            lexer.traverseWhitespace(true)
            when {
                lexer.isEOF() -> { contextStack.pop(); continue; }
                lexer.currentChar == '#' -> throw LexerException(lexer, LexicalError.PreprocessorError)
                lexer.currentChar == '}' -> {
                    lexer.moveForward(); lexer.traverseWhitespace(false)
                    if(lexer.currentChar != ';') throw lexer.unexpectedInputException()
                    contextStack.pop(); continue
                }
            }
            var keyword: String = lexer.readIdentifier()
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
                    with(ParamMutableClassImpl(currentContext, this, keyword, baseClass, mutableListOf())) {
                        currentContext += this
                        contextStack.push(this)
                    }
                    continue
                }
                else -> {
                    if (lexer.currentChar == '[') { lexer.moveForward(); lexer.traverseWhitespace()
                        if (lexer.currentChar != ']') { throw lexer.unexpectedInputException() }; lexer.traverseWhitespace()
                        val operator: ParamOperatorTypes = lexer.readOperator(); lexer.moveForward(); lexer.traverseWhitespace()
                        currentContext.slimCommands.add(ParamMutableVariableStatementImpl(currentContext, this, keyword).apply {
                            slimValue = lexer.readArray(this, this@ParamParser)
                            slimOperator = operator
                        })
                        while (lexer.currentChar == ';') lexer.moveForward()
                        continue
                    } else if (lexer.currentChar == '=') {
                        currentContext.slimCommands.add(
                            ParamMutableVariableStatementImpl(currentContext, this, keyword).apply {
                                slimValue = lexer.readLiteral(this, this@ParamParser, ';')
                            }
                        )
                        lexer.traverseWhitespace()
                        if (lexer.currentChar != ';') throw lexer.unexpectedInputException()
                        continue
                    }
                    throw lexer.unexpectedInputException()
                }
            }
        }
    }

    override fun getParamElementType(): ParamElementTypes = ParamElementTypes.FILE

    override fun isBinarizable(): Boolean = super.isBinarizable()

    override fun isCurrentlyValid(): Boolean = super.isCurrentlyValid()

    override fun toParam(): String = super.toParam()
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
//                }
//            }
//        }
//    }
//
//        return file
//    }




}
