package com.flipperplz.bisutils.param.utils.extensions

import com.flipperplz.bisutils.BisPreProcessor
import com.flipperplz.bisutils.param.ParamFile
import com.flipperplz.bisutils.param.literal.ParamArray
import com.flipperplz.bisutils.param.literal.ParamString
import com.flipperplz.bisutils.param.node.ParamElement
import com.flipperplz.bisutils.param.node.ParamLiteralBase
import com.flipperplz.bisutils.param.statement.ParamClass
import com.flipperplz.bisutils.param.statement.ParamDeleteStatement
import com.flipperplz.bisutils.param.statement.ParamEnum
import com.flipperplz.bisutils.param.statement.ParamExternalClass
import com.flipperplz.bisutils.param.utils.ParamOperatorTypes
import com.flipperplz.bisutils.param.utils.ParamStringType
import com.flipperplz.bisutils.param.utils.mutability.*
import com.flipperplz.bisutils.param.utils.mutability.node.ParamMutableStatementHolder
import com.flipperplz.bisutils.utils.BisLexer
import java.util.Stack
import kotlin.math.ceil
import kotlin.math.floor

object ParamParseUtils {

    operator fun ParamFile.Companion.invoke(name: String, lexer: BisLexer, preProcessor: BisPreProcessor): ParamFile {
        if(!preProcessor.processText(lexer)) throw Exception("Preprocess failed.")
        val file = ParamMutableFile(name)
        val contextStack = Stack<ParamMutableStatementHolder>().apply { push(file) }
        var currentContext: ParamMutableStatementHolder

        with(lexer) {
            while (true) {
                currentContext = contextStack.peek()
                moveForward()
                skipWhile { it.isWhitespace() }
                when {
                    currentChar == '#' -> throw Exception("${getPositionstring()} Error: Unexpected directive '${getWhile { !isWhitespace() }}'.")
                    currentChar == '}' -> {
                        moveForward()
                        skipWhile { (it.isWhitespace() || it.currentChar == ';') && !it.isEOF() }
                        if(isEOF()) moveBackward()
                        contextStack.pop()
                        contextStack.peek().slimCommands.add(currentContext as? ParamClass ?: throw Exception())
                        if(contextStack.isEmpty())  break
                        continue
                    }
                    !isEOF() -> moveBackward()
                }

                val word = readIdentifier()
                when (word) {
                    "delete" -> {
                        val context = readIdentifier()
                        skipWhile { it.isWhitespace() }
                        if(currentChar != ';') throw Exception("${getPositionstring()} Error: Missing ';' after delete statement.")
                        val delete = ParamMutableDeleteStatement(slimName = context, slimParent = currentContext, containingParamFile = file)
                        currentContext.slimCommands.add(delete as ParamDeleteStatement)
                        continue
                    }
                    "class" -> {
                        val className = readIdentifier()
                        var base = ""
                        skipWhile { it.isWhitespace() }
                        when(currentChar) {
                            ';' -> {
                                val external = ParamMutableExternalClass(slimParent = currentContext, slimName = className)
                                currentContext.slimCommands.add(external as ParamExternalClass)
                                continue
                            }
                            ':' -> base = readIdentifier()
                            else -> {}
                        }
                        if(currentChar != '{') throw Exception("${getPositionstring()} Error: Unexpected input '$currentChar', expected ';', ':', or '{'.")
                        contextStack.push(ParamMutableClass(
                            slimParent = currentContext,
                            containingParamFile = file,
                            className,
                            base,
                            mutableListOf()
                        ))
                        continue
                    }
                    "enum" -> {
                        readIdentifier()
                        skipWhile { it.isWhitespace() }
                        if(currentChar != '{') throw Exception("${getPositionstring()} Error: expected '{' instead got '$currentChar'.")

                        var enumValue = 0
                        val enum = ParamMutableEnum(slimParent = currentContext, enumValues = mutableMapOf())
                        do {
                            val valueName = readIdentifier()
                            skipWhile { it.isWhitespace() }
                            if(currentChar == '=') Unit //TODO: Set Enum Value
                            enum.enumValues?.set(name, enumValue)
                            enumValue++
                            skipWhile { it.isWhitespace() }
                        } while (currentChar == ',')
                        currentContext.slimCommands.add(enum as ParamEnum)
                    }
                    "__EXEC" -> {
                        skipWhile { it.isWhitespace() }
                        if(currentChar != '(') throw Exception("${getPositionstring()} Error: expected '(' instead got '$currentChar'.")
                        val expression = getUntil { it.currentChar == ')' }.trimEnd(')', ' ')
                        skipWhile { it.currentChar == ';' || it.isWhitespace() }
                        continue
                    }
                    else ->  {
                        skipWhile { it.isWhitespace() }
                        val identifier = readIdentifier()
                        if(currentChar == '[') {
                            skipWhile { it.isWhitespace() }
                            if(currentChar != ']')
                                throw Exception("${getPositionstring()} Error: Unexpected character '$currentChar', expected ']'.")
                            skipWhile { it.isWhitespace() }
                            val operatorText = getWhile {
                                it.currentChar == '=' ||
                                it.currentChar == '+' ||
                                it.currentChar == '-'
                            }
                            val operator: ParamOperatorTypes = when(operatorText) {
                                "=" -> ParamOperatorTypes.ASSIGN
                                "+=" -> ParamOperatorTypes.ADD_ASSIGN
                                "-=" -> ParamOperatorTypes.SUB_ASSIGN
                                else -> throw Exception("${getPositionstring()} Error: Unexpected operator '$operatorText', expected '=', '+=', or '-='.")
                            }
                            currentContext.slimCommands.add(ParamMutableVariableStatement(currentContext, file, identifier).apply {
                                slimValue = readArray(lexer, this, file)
                            })
                        } else if(currentChar == '=') {
                            currentContext.slimCommands.add(ParamMutableVariableStatement(currentContext, file, identifier).apply {
                                slimValue = readLiteral(lexer, this, file, ';')
                            })
                            continue
                        } else throw Exception("${getPositionstring()} Error: Unexpected character '$currentChar', expected '=' or '['.")
                        continue
                    }
                }
            }
        }

        return file
    }

    private fun readArray(lexer: BisLexer, paramMutableVariableStatement: ParamMutableVariableStatement, file: ParamMutableFile): ParamArray? {
        TODO()
    }

    private fun readLiteral(lexer: BisLexer, parent: ParamElement, file: ParamFile, vararg delimiters: Char): ParamLiteralBase {
        lexer.skipWhile { it.isWhitespace() }
        lexer.readParamString(parent, file, *delimiters).also { string ->
            return if(string.slimStringType == ParamStringType.QUOTED) string else with(string.slimValue?.toFloatOrNull()) {
                if(this != null) {
                    if(ceil(this/3) == floor(this/3) && this <= Int.MAX_VALUE)
                        ParamMutableInt(parent, file, this.toInt())
                    else ParamMutableFloat(parent, file, this)
                } else string
            }
        }
    }

    fun BisLexer.readParamString(parent: ParamElement, file: ParamFile, vararg delimiters: Char): ParamString {
        skipWhile { it.isWhitespace() }
        val type = if(currentChar == '\"') ParamStringType.QUOTED else ParamStringType.UNQUOTED
        val builder = StringBuilder()
        while (with(moveForward()) {
            !delimiters.contains(this)
        }) {
            if(currentChar == '"' && type == ParamStringType.QUOTED) {
                //TODO(Bohemia): you guys seriously need to take another look at this logic...
                if(moveForward() != '"') {
                    skipWhile { it.isWhitespace() }
                    if(currentChar != '\\') break
                    if(moveForward() != 'n') throw Exception("${getPositionstring()} Error: Unexpected character '$currentChar', expected 'n'")
                    skipWhile { it.isWhitespace() }
                    if(currentChar != '"') throw Exception("${getPositionstring()} Error: Unexpected character '$currentChar', expected 'n'")
                    builder.append('\"')
                }
                builder.append(currentChar)
            }
            if(isEOL() || isEOF()) throw Exception("${getPositionstring()} Error: End of line/file encountered.")
            builder.append(currentChar)
        }

        return ParamMutableString(parent, file, type, builder.toString())
    }

    fun BisLexer.readIdentifier(): String {
        skipWhile { it.isWhitespace() }
        return getWhile { it.isAlphaNumeric() || it.currentChar == '_' }
    }

}