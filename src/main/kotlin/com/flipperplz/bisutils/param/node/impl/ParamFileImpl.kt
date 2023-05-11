package com.flipperplz.bisutils.param.node.impl

import com.flipperplz.bisutils.param.node.RapElement
import com.flipperplz.bisutils.param.node.RapFile
import com.flipperplz.bisutils.param.node.RapStatement
import java.util.Stack

class ParamFileImpl(
    override val fileName: String,
) : RapFile {
    override val slimParent: RapElement? = null
    override lateinit var slimEnum: Map<String, Int>
    override lateinit var slimCommands: List<RapStatement>

    companion object {

        fun parse(text: String) : ParamFileImpl {
            val line: Int = 1
            val col: Int = 0
            val currentWord: String
            val previousWord: String
            TODO()
        }
    }
}

class BisParser(
    buffer: String
) {
    val stack: ArrayDeque<Char> = ArrayDeque<Char>(buffer.toCharArray().toList())
    var bufferPtr: Int = 0
        private set
    var currentChar: Char = Char.MIN_VALUE
        private set
    var previousChar: Char = Char.MIN_VALUE
        private set
    var line: Int = 0
        private set
    var col: Int = 0
        private set

    fun getChar(): Char {
        if(stack.isEmpty()) {
            previousChar = currentChar
            currentChar = Char.MIN_VALUE

            return Char.MIN_VALUE
        }

        previousChar = currentChar
        currentChar = stack.removeFirst()
        with(stack.removeFirst()) {
            currentChar = this
            return this
        }
    }

    fun ungetChar() {
        if(bufferPtr <= 0) return
        bufferPtr--
        stack.addFirst(currentChar)
    }

    private fun decrementCurrent() {
        bufferPtr--
        if (currentChar == '\n') line--
        else col--
    }


    private fun incrementCurrent() {
        bufferPtr++
        if (currentChar == '\n') {
            line++
            col = 0
        } else {
            col++
        }
    }


}