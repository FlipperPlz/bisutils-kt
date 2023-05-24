package com.flipperplz.bisutils.param.lexer

import com.flipperplz.bisutils.param.ParamFile
import com.flipperplz.bisutils.param.ast.literal.ParamString
import com.flipperplz.bisutils.param.ast.node.ParamElement
import com.flipperplz.bisutils.param.ast.node.ParamLiteralBase
import com.flipperplz.bisutils.param.utils.ParamOperatorTypes
import com.flipperplz.bisutils.param.utils.ParamStringType
import com.flipperplz.bisutils.param.utils.extensions.isBlank
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
    fun readIdentifier(allowEOF: Boolean = false): String {
        traverseWhitespace()
        if(currentChar == null && !allowEOF) throw eofException()
        val builder = StringBuilder()
        while (with(currentChar) { this != null && (isLetterOrDigit() || this == '_')  }) {
            builder.append(currentChar)
            moveForward()
            if(currentChar == null) throw eofException()
        }

        return builder.toString()
    }

    @Throws(LexerException::class)
    fun readOperator(): ParamOperatorTypes = when (currentChar) {
        '+' -> { if(moveForward() == '=') ParamOperatorTypes.ADD_ASSIGN else throw unexpectedInputException() }
        '-' -> { if(moveForward() == '=') ParamOperatorTypes.ADD_ASSIGN else throw unexpectedInputException() }
        '=' -> ParamOperatorTypes.ASSIGN
        else -> throw unexpectedInputException()
    }

    @Throws(LexerException::class)
    fun readString(vararg delimiters: Char): String {
        traverseWhitespace()
        val quoted = if(currentChar == '\"') true.also { moveForward() } else false
        val builder = StringBuilder()
        while (!delimiters.contains(currentChar ?: throw eofException())) {
            if((currentChar == '\n' || currentChar == '\r')) {
               if(!quoted) {
                   traverseWhitespace()
                   if(currentChar != '#') break
                    //TODO: Preprocess line
                   break
               }
                throw eolException()
            }

            if(currentChar == '"' && quoted) {
                if(moveForward() != '"') {
                    traverseWhitespace()
                    if(currentChar != '\\') return "\"${builder.toString()}\""
                    if(moveForward() != 'n') throw unexpectedInputException()
                    traverseWhitespace()
                    if(currentChar != '"') throw unexpectedInputException()
                    builder.append('\n')
                    moveForward()
                    continue
                }
            }
            builder.append(currentChar)
            moveForward()
        }

        return if(quoted) "\"${builder.toString()}\"" else builder.toString()
    }

    @Throws(LexerException::class)
    fun readString(parent: ParamElement, file: ParamFile, vararg delimiters: Char): ParamString {
        var string = readString(*delimiters)
        val type = if(string.startsWith('"')) ParamStringType.QUOTED.also {
            string = string.trimStart('"').trimEnd('"')
        } else ParamStringType.UNQUOTED
        return ParamMutableStringImpl(parent, file, type, string)
    }

    @Throws(LexerException::class)
    fun readLiteral(parent: ParamElement, file: ParamFile, vararg delimiters: Char): ParamLiteralBase = readString(parent, file, *delimiters).also { string ->
        return if(string.slimStringType == ParamStringType.QUOTED) string else with(string.slimValue?.toFloatOrNull()) {
            if(this != null) {
                if(ceil(this/3) == floor(this/3) && this <= Int.MAX_VALUE)
                    ParamMutableIntImpl(parent, file, this.toInt())
                else ParamMutableFloatImpl(parent, file, this)
            } else string
        }
    }

    @Throws(LexerException::class)
    fun readArray(parent: ParamElement, file: ParamMutableFile): ParamMutableArray {
        val array = ParamMutableArrayImpl(parent, file)
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
                    when(currentChar) {
                        '}' -> contextStack.pop()
                        ',' -> continue
                        else -> throw unexpectedInputException()
                    }
                }
                ',' -> continue
                else -> {
                    currentContext += readLiteral(currentContext, file, ';', '}')
                    traverseWhitespace()
                    when(currentChar) {
                        '}' -> contextStack.pop()
                        ',' -> continue
                        else -> throw unexpectedInputException()
                    }
                }
            }
        }
        moveForward()
        return array
    }

    @Throws(LexerException::class)
    fun traverseWhitespace(allowEOF: Boolean = false): Int {
        var count: Int = 0
        while(true){
            if(isEOF()) {
                if(allowEOF) break
                else throw eofException()
            }
            when(currentChar) {
                '\r' -> { line++; count++; if(moveForward() != '\n') continue  }
                '\n' -> { line++; count++; moveForward() }
                else -> { if(!whitespaces.contains(currentChar)) break else moveForward().also { count++ }; }
            }
        }
        return count;
    }

}