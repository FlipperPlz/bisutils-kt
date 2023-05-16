package com.flipperplz.bisutils.param.utils.extensions

import com.flipperplz.bisutils.BisPreProcessor
import com.flipperplz.bisutils.param.ParamFile
import com.flipperplz.bisutils.param.node.ParamElement
import com.flipperplz.bisutils.param.node.ParamStatementHolder
import com.flipperplz.bisutils.param.statement.ParamDeleteStatement
import com.flipperplz.bisutils.param.statement.ParamExternalClass
import com.flipperplz.bisutils.param.utils.mutability.ParamMutableClass
import com.flipperplz.bisutils.param.utils.mutability.ParamMutableDeleteStatement
import com.flipperplz.bisutils.param.utils.mutability.ParamMutableExternalClass
import com.flipperplz.bisutils.param.utils.mutability.ParamMutableFile
import com.flipperplz.bisutils.param.utils.mutability.node.ParamMutableStatementHolder
import com.flipperplz.bisutils.utils.BisLexer
import java.util.Stack

object ParamParseUtils {

    operator fun ParamFile.Companion.invoke(name: String, lexer: BisLexer, preProcessor: BisPreProcessor): ParamFile {
        if(!preProcessor.processText(lexer)) throw Exception("Preprocess failed.")
        val file = ParamMutableFile(name)
        val contextStack = Stack<ParamMutableStatementHolder>().apply { push(file) }

        with(lexer) {
            while (true) {
                moveForward()
                skipWhile { it.isWhitespace() }
                when {
                    currentChar == '#' -> throw Exception("${getPositionstring()} Error: Unexpected directive `${getWhile { !isWhitespace() }}`.")
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
                        val delete = ParamMutableDeleteStatement(slimName = context, slimParent = contextStack.peek(), containingParamFile = file)
                        contextStack.peek().slimCommands.add(delete as ParamDeleteStatement)
                        continue
                    }
                    "class" -> {
                        val name = readIdentifier()
                        var base = ""
                        skipWhile { it.isWhitespace() }
                        when(currentChar) {
                            ';' -> {
                                val external = ParamMutableExternalClass(slimParent = contextStack.peek(), slimName = name)
                                contextStack.peek().slimCommands.add(external as ParamExternalClass)
                                continue
                            }
                            ':' -> base = readIdentifier()
                            else -> {}
                        }
                        if(currentChar != '{') throw Exception("${getPositionstring()} Error: Unexpected input '$currentChar', expected ';', ':', or '{'.")
                        contextStack.push(ParamMutableClass(
                            slimParent = contextStack.peek(),
                            containingParamFile = file,
                            name,
                            base,
                            mutableListOf()
                        ))
                        continue
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