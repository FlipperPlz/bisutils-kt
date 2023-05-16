package com.flipperplz.bisutils.param.utils.extensions

import com.flipperplz.bisutils.BisPreProcessor
import com.flipperplz.bisutils.param.ParamFile
import com.flipperplz.bisutils.param.node.ParamElement
import com.flipperplz.bisutils.param.node.ParamStatementHolder
import com.flipperplz.bisutils.param.statement.ParamDeleteStatement
import com.flipperplz.bisutils.param.statement.ParamExternalClass
import com.flipperplz.bisutils.param.utils.mutability.*
import com.flipperplz.bisutils.param.utils.mutability.node.ParamMutableStatementHolder
import com.flipperplz.bisutils.utils.BisLexer
import java.util.Stack

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
                        if(contextStack.isEmpty())  break
                    }
                    !isEOF() -> moveBackward()
                }

                when (readIdentifier()) {
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
                        currentContext.slimCommands.add(enum)
                    }
                }
            }
        }

        return file
    }

    fun BisLexer.readIdentifier(): String {
        skipWhile { it.isWhitespace() }
        return getWhile { it.isAlphaNumeric() || it.currentChar == '_' }
    }

}