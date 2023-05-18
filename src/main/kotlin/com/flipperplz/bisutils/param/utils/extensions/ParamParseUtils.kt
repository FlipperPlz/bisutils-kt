package com.flipperplz.bisutils.param.utils.extensions

import com.flipperplz.bisutils.BisPreProcessor
import com.flipperplz.bisutils.param.ParamFile
import com.flipperplz.bisutils.param.literal.ParamArray
import com.flipperplz.bisutils.param.literal.ParamString
import com.flipperplz.bisutils.param.node.ParamElement
import com.flipperplz.bisutils.param.node.ParamLiteralBase
import com.flipperplz.bisutils.param.statement.*
import com.flipperplz.bisutils.param.utils.ParamOperatorTypes
import com.flipperplz.bisutils.param.utils.ParamStringType
import com.flipperplz.bisutils.param.utils.extensions.ParamSlimUtils.toMutableArray
import com.flipperplz.bisutils.param.utils.mutability.*
import com.flipperplz.bisutils.param.utils.mutability.node.ParamMutableStatementHolder
import com.flipperplz.bisutils.utils.BisLexer
import java.util.Stack
import kotlin.math.ceil
import kotlin.math.floor

fun readParam(name: String, lexer: BisLexer, preProcessor: BisPreProcessor?): ParamFile {
    if(preProcessor?.processText(lexer) == false) throw Exception("Preprocess failed.")

    val file = ParamMutableFile(name)
    val contextStack = Stack<ParamMutableStatementHolder>().apply { push(file) }
    var currentContext: ParamMutableStatementHolder

    with(lexer) {
        while (contextStack.isNotEmpty()) {
            currentContext = contextStack.peek()

            val c = moveForward()
            when {
                isWhitespace() || isEOL() -> continue
                isEOF() -> { contextStack.pop(); continue; }
                c == '#' -> throw Exception("${getPositionstring()} Error: Unexpected directive '${getWhile { !isWhitespace() }}'.")
                c == '}' -> {
                    moveForward()
                    while (isWhitespace() && !isEOF()) moveForward()
                    if(isEOF()) throw Exception("${getPositionstring()} Error: Premature end of file, expected ';'.")
                    if(currentChar != ';') throw Exception("${getPositionstring()} Error: Unknown input '$currentChar', expected ';'.")
                    contextStack.pop()
                    continue
                }
                else -> moveBackward()
            }


            val keyword = readIdentifier()
            when (keyword) {
                "delete" -> {
                    val context = readIdentifier()
                    if(!traverseWhitespace(false))
                        throw Exception("${lexer.getPositionstring()} Error: Premature end of file, expected identifier.")
                    if(currentChar != ';') throw Exception("${getPositionstring()} Error: Missing ';' after delete statement.")
                    val delete = ParamMutableDeleteStatement(slimName = context, slimParent = currentContext, containingParamFile = file)
                    currentContext.slimCommands.add(delete as ParamDeleteStatement)
                    continue
                }
                "class" -> {
                    val className = readIdentifier()
                    var base = ""
                    if(!traverseWhitespace(false))
                        throw Exception("${lexer.getPositionstring()} Error: Premature end of file, expected ';', ':', or '}'.")

                    when(currentChar) {
                        ';' -> {
                            val external = ParamMutableExternalClass(slimParent = currentContext, slimName = className)
                            currentContext.slimCommands.add(external as ParamExternalClass)
                            continue
                        }
                        ':' -> {
                            moveForward()
                            base = readIdentifier()
                            if(!traverseWhitespace(false))
                                throw Exception("${lexer.getPositionstring()} Error: Premature end of file, expected ';', ':', or '{'.")
                            if(lexer.currentChar != '{')
                                throw Exception("${lexer.getPositionstring()} Error: Unexpected input '$currentChar', expected '{'.")
                        }
                        '{' -> { }
                        else -> throw Exception("${lexer.getPositionstring()} Error: Unexpected input '$currentChar', expected ';', ':', or '{'.")
                    }
                    val clazz = ParamMutableClass(
                        slimParent = currentContext,
                        containingParamFile = file,
                        className,
                        base,
                        mutableListOf()
                    )
                    contextStack.push(clazz).also {
                        currentContext.slimCommands.add(it as ParamClass)
                    }
                    continue
                }
                "enum" -> {
                    readIdentifier()
                    if(!traverseWhitespace(false))
                        throw Exception("${lexer.getPositionstring()} Error: Premature end of file, expected '{'.")
                    if(currentChar != '{') throw Exception("${getPositionstring()} Error: expected '{' instead got '$currentChar'.")

                    var enumValue = 0
                    val enum = ParamMutableEnum(slimParent = currentContext, enumValues = mutableMapOf())
                    do {
                        @Suppress("UNUSED_VARIABLE") val enumName = readIdentifier()
                        if(!traverseWhitespace(false))
                            throw Exception("${lexer.getPositionstring()} Error: Premature end of file, expected '='.")
                        if(currentChar == '=') Unit //TODO: Set Enum Value
                        enum.enumValues[name] = enumValue
                        enumValue++
                        if(!traverseWhitespace(false))
                            throw Exception("${lexer.getPositionstring()} Error: Premature end of file, expected ',' or '}'.")
                    } while (currentChar == ',')
                    currentContext.slimCommands.add(enum as ParamEnum)
                }
                "__EXEC" -> {
                    if(!traverseWhitespace(false))
                        throw Exception("${lexer.getPositionstring()} Error: Premature end of file, expected '('.")
                    if(currentChar != '(') throw Exception("${getPositionstring()} Error: expected '(' instead got '$currentChar'.")
                    @Suppress("UNUSED_VARIABLE") //TODO(Ryann): Encapsulate parser and add abstraction for validating SQF
                    val expression = getUntil { it.currentChar == ')' }.trimEnd(')', ' ')

                    skipWhile { currentChar == ';' || isWhitespace() }
                    continue
                }
                else ->  {
                    if(currentChar == '[') {
                        if(!traverseWhitespace(false))
                            throw Exception("${lexer.getPositionstring()} Error: Premature end of file, expected ']'.")
                        if(currentChar != ']')
                            throw Exception("${getPositionstring()} Error: Unexpected character '$currentChar', expected ']'.")
                        if(!traverseWhitespace(false))
                            throw Exception("${lexer.getPositionstring()} Error: Premature end of file, expected '=', '+=', or '-='.")
                        val operatorText = getWhile {
                            currentChar == '=' || currentChar == '+' || currentChar == '-'
                        }
                        val operator: ParamOperatorTypes = when(operatorText) {
                            "=" -> ParamOperatorTypes.ASSIGN
                            "+=" -> ParamOperatorTypes.ADD_ASSIGN
                            "-=" -> ParamOperatorTypes.SUB_ASSIGN
                            else -> throw Exception("${getPositionstring()} Error: Unexpected operator '$operatorText', expected '=', '+=', or '-='.")
                        }
                        currentContext.slimCommands.add(
                            ParamMutableVariableStatement(
                                currentContext, file, keyword
                            ).apply {
                                slimValue = readArray(lexer, this, file)
                                slimOperator = operator
                            } as ParamVariableStatement
                        )
                        skipWhile { currentChar == ';' || isWhitespace() }

                    } else if(currentChar == '=') {
                        currentContext.slimCommands.add(ParamMutableVariableStatement(currentContext, file, keyword).apply {
                            slimValue = readLiteral(lexer, this, file, ';')
                        })
                        traverseWhitespace(false)
                        if(currentChar != ';')
                            throw Exception("${getPositionstring()} Error: Unexpected character '$currentChar', expected ';'.")

                        continue
                    } else throw Exception("${getPositionstring()} Error: Unexpected character '$currentChar', expected '=' or '['.")
                    continue
                }
            }
        }
    }

    return file
}

private fun readArray(lexer: BisLexer, parent: ParamElement, file: ParamMutableFile): ParamArray {
    val array = ParamMutableArray(parent, file)
    val contextStack = Stack<ParamMutableArray>().apply { push(array) }
    var currentContext: ParamMutableArray

    if(lexer.moveForward() != '{')  throw Exception("${lexer.getPositionstring()} Error: Unexpected character '${lexer.currentChar}', expected '{'")
    while (contextStack.isNotEmpty()) {
        currentContext = contextStack.peek()
        lexer.traverseWhitespace()

        when(lexer.moveForward()) {
            '{' -> {
                lexer.moveBackward()

                currentContext.slimValue?.add(
                    contextStack.push(
                        readArray(lexer, array, file).toMutableArray()
                    )
                )
                lexer.traverseWhitespace()

                if(with(lexer.peekForward() ) { this != ',' && this != '}'} )
                    throw Exception("${lexer.getPositionstring()} Error: Unexpected character '${lexer.currentChar}', expected ',', or '}'")
                continue
            }
            ',' -> continue
            '}' -> {
                contextStack.pop()
            }
            else -> {
                lexer.moveBackward()
                currentContext.slimValue?.add(
                    readLiteral(lexer, currentContext, file, ';', '}')
                )
            }
        }
    }
    return array
}

private fun readLiteral(lexer: BisLexer, parent: ParamElement, file: ParamFile, vararg delimiters: Char): ParamLiteralBase {
    readParamString(lexer, parent, file, *delimiters).also { string ->
        return if(string.slimStringType == ParamStringType.QUOTED) string else with(string.slimValue?.toFloatOrNull()) {
            if(this != null) {
                if(ceil(this/3) == floor(this/3) && this <= Int.MAX_VALUE)
                    ParamMutableInt(parent, file, this.toInt())
                else ParamMutableFloat(parent, file, this)
            } else string
        }
    }
}

fun readParamString(lexer: BisLexer, parent: ParamElement, file: ParamFile, vararg delimiters: Char): ParamString {
    if(!lexer.traverseWhitespace(true))
        throw Exception("${lexer.getPositionstring()} Error: Premature end of file, expected literal.")
    val type = if(lexer.peekForward() == '\"')
        ParamStringType.QUOTED.also {
            lexer.moveForward()
        } else ParamStringType.UNQUOTED
    val builder = StringBuilder()
    while (with(lexer.moveForward()) { !delimiters.contains(this) }) {

        if(lexer.currentChar == '"' && type == ParamStringType.QUOTED) {
            //TODO(Bohemia): you guys seriously need to take another look at this logic...
            if(lexer.moveForward() != '"') {
                if(!lexer.traverseWhitespace(true))
                    throw Exception("${lexer.getPositionstring()} Error: Premature end of file, expected literal.")

                if(lexer.moveForward() != '\\')
                    continue
                if(lexer.moveForward() != 'n')
                    throw Exception("${lexer.getPositionstring()} Error: Unexpected character '${lexer.currentChar}', expected 'n'")
                if(!lexer.traverseWhitespace(true))
                    throw Exception("${lexer.getPositionstring()} Error: Premature end of file, expected literal.")
                if(lexer.isNextEOF())

                if(lexer.currentChar != '"') throw Exception("${lexer.getPositionstring()} Error: Unexpected character '${lexer.currentChar}', expected '\"'")
                builder.append('\"')
            }
            builder.append(lexer.currentChar)
            continue
        }
        if(lexer.isEOL() || lexer.isEOF()) throw Exception("${lexer.getPositionstring()} Error: End of line/file encountered.")
        builder.append(lexer.currentChar)
    }

    return ParamMutableString(parent, file, type, builder.toString())
}

fun BisLexer.readIdentifier(): String {
    if((isWhitespace() || isEOL()) && !traverseWhitespace(true))
        throw Exception("${getPositionstring()} Error: Premature end of file, expected identifier.")

    val builder = StringBuilder()
    do {
        moveForward()
    } while ((currentChar.isLetterOrDigit() || currentChar == '_') && with(builder.append(currentChar)) {true})

    return builder.toString()
}