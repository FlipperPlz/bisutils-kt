package com.flipperplz.bisutils.stringtable

import com.flipperplz.bisutils.parsing.BisLexer
import com.flipperplz.bisutils.preprocesser.boost.BoostPreprocessor

class StringTableLexer(text: String) : BisLexer(text) {


    fun readCSVColumn(): String {
        if(bufferPtr == -1) moveForward()
        skipWhitespaces()
        val colBuilder = StringBuilder();
        //TODO: Read Column
        return colBuilder.toString()
    }

    fun skipWhitespaces(): Int {
        var i = 0
        while (BoostPreprocessor.whitespaces.contains(currentChar) && currentChar != '\u0D00' && currentChar != '\u0A00') {
            moveForward(); i++
        }
        return i
    }
}