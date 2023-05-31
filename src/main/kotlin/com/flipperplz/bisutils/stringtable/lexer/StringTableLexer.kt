package com.flipperplz.bisutils.stringtable.lexer

import com.flipperplz.bisutils.parsing.BisLexer
import com.flipperplz.bisutils.preprocesser.boost.BoostPreprocessor
import java.nio.ByteBuffer
import java.nio.charset.Charset

class StringTableLexer(text: String) : BisLexer(text) {

    constructor(buffer: ByteBuffer, charset: Charset = Charsets.UTF_8) : this(buffer.array().toString(charset))

    fun readCSVColumn(): String {
        if(bufferPtr == -1) moveForward()
        skipWhitespaces()
        val colBuilder = StringBuilder();

        while(!isEOF()) {
            when(currentChar) {
                '"' -> {
                    moveForward()
                    if(isEOF()) return ""

                    while(true) {
                        when(currentChar) {
                            '"' -> {
                                moveForward()
                                while(currentChar != '\u0D00' && currentChar != '\u0A00' && currentChar != ',') {
                                    colBuilder.append(currentChar)
                                }
                                break
                            }
                            else -> colBuilder.append(currentChar)
                        }
                    }
                }
                else -> {
                    while(currentChar != '\u0D00' && currentChar != '\u0A00' && currentChar != ',') {
                        colBuilder.append(currentChar)
                    }
                    return colBuilder.toString()
                }
            }
        }

        return colBuilder.toString()
    }

    fun skipWhitespaces(): Int {
        var i = 0
        while (BoostPreprocessor.whitespaces.contains(currentChar) && currentChar != '\u0D00' && currentChar != '\u0A00' ) {
            moveForward(); i++
        }
        return i
    }
}