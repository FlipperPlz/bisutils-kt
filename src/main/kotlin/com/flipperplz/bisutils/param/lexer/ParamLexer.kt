package com.flipperplz.bisutils.param.lexer

import com.flipperplz.bisutils.param.ParamFile
import com.flipperplz.bisutils.param.ast.literal.ParamString
import com.flipperplz.bisutils.param.ast.node.ParamElement
import com.flipperplz.bisutils.param.ast.node.ParamLiteralBase
import com.flipperplz.bisutils.param.utils.ParamStringType
import com.flipperplz.bisutils.param.utils.extensions.plusAssign
import com.flipperplz.bisutils.param.utils.mutability.*
import com.flipperplz.bisutils.parsing.BisLexer
import com.flipperplz.bisutils.parsing.LexerException
import java.util.*
import kotlin.math.ceil
import kotlin.math.floor

class ParamLexer(paramText: String) : BisLexer(paramText) {
    companion object {
        val whitespaces: List<Char> = mutableListOf(' ', '\t', '\u000B', '\u000C')
    }
    var line: Int = 1
        private set


    @Throws(LexerException::class)
    fun readIdentifier(): String {
        traverseWhitespace()
        if(currentChar == null) throw eofException()
        val builder = StringBuilder()
        while (with(currentChar!!) { isLetterOrDigit() || this == '_'  }) {
            builder.append(currentChar)
            moveForward()
            if(currentChar == null) throw eofException()
        }

        return builder.toString()
    }

    @Throws(LexerException::class)
    fun readString(parent: ParamElement, file: ParamFile, vararg delimiters: Char): ParamString {
        traverseWhitespace()
        val type = if(currentChar == '\"')
            ParamStringType.QUOTED.also {
                moveForward()
            } else ParamStringType.UNQUOTED
        val builder = StringBuilder()
        while (!delimiters.contains(currentChar ?: throw eofException())) {
            if(currentChar == '"' && type == ParamStringType.QUOTED) {
                //TODO(Bohemia): you guys seriously need to take another look at this logic...
                if(moveForward() != '"') {
                    traverseWhitespace()
                    if(currentChar != '\\') continue
                    if(moveForward() != 'n') throw unexpectedInputException()
                    traverseWhitespace()
                    if(currentChar != '"') throw unexpectedInputException()
                    builder.append('\"')
                }
            }

            builder.append(currentChar)
            moveForward()
        }

        return ParamMutableString(parent, file, type, builder.toString())
    }

    @Throws(LexerException::class)
    fun readLiteral(parent: ParamElement, file: ParamFile, vararg delimiters: Char): ParamLiteralBase = readString(parent, file, *delimiters).also { string ->
        return if(string.slimStringType == ParamStringType.QUOTED) string else with(string.slimValue?.toFloatOrNull()) {
            if(this != null) {
                if(ceil(this/3) == floor(this/3) && this <= Int.MAX_VALUE)
                    ParamMutableInt(parent, file, this.toInt())
                else ParamMutableFloat(parent, file, this)
            } else string
        }
    }

    @Throws(LexerException::class)
    fun readArray(parent: ParamElement, file: ParamMutableFile): ParamMutableArray {
        val array = ParamMutableArray(parent, file)
        val contextStack = Stack<ParamMutableArray>().apply { push(array); traverseWhitespace() }
        if(currentChar != '{') throw unexpectedInputException()
        while (contextStack.isNotEmpty()) {
            val currentContext = contextStack.peek()
            moveForward()
            traverseWhitespace()

            when(currentChar) {
                '{' -> {
                    moveBackward().also { currentContext += contextStack.push(readArray(array, file)) }
                    traverseWhitespace()

                    if(currentChar == ',' || currentChar == '}' ) { moveBackward(); continue }
                    throw unexpectedInputException()
                }
                ',' -> continue
                '}' -> contextStack.pop()
                else -> currentContext += readLiteral(currentContext, file, ';', '}')
            }
        }
        return array
    }

    @Throws(LexerException::class)
    fun traverseWhitespace(allowEOF: Boolean = false) {
        while(true){
            if(isEOF()) { if(allowEOF) break else throw eofException() }
            when(currentChar) {
                '\r' -> { line++; if(moveForward() != '\n') continue else moveForward() }
                '\n' -> { line++; moveForward() }
                else -> { if(!whitespaces.contains(currentChar)) break else moveForward() }
            }
        }
    }

}