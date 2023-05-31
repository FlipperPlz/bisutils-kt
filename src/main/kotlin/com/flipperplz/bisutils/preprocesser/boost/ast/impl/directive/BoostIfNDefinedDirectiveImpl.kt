package com.flipperplz.bisutils.preprocesser.boost.ast.impl.directive

import com.flipperplz.bisutils.parsing.BisLexer
import com.flipperplz.bisutils.preprocesser.boost.BoostPreprocessor
import com.flipperplz.bisutils.preprocesser.boost.ast.directive.BoostIfNDefinedDirective
import com.flipperplz.bisutils.preprocesser.boost.ast.element.BoostMacroElement
import com.flipperplz.bisutils.preprocesser.boost.ast.impl.element.BoostMacroElementImpl

class BoostIfNDefinedDirectiveImpl(lexer: BisLexer, override val processor: BoostPreprocessor) :
    BoostIfNDefinedDirective {
    override lateinit var macroName: String
        private set
    override lateinit var ifBody: String
        private set
    override var elseBody: String? = null
        private set

    init {
        BoostPreprocessor.traverseWhitespace(lexer)
        macroName = BoostMacroElementImpl.readMacroID(lexer, true)
        BoostPreprocessor.traverseWhitespace(lexer, allowEOF = false, allowEOL = false, allowDirectiveEOL = true)

        val builder = StringBuilder()

        var elseEncountered = false
        while (true) {
            if(lexer.isEOF()) throw lexer.eofException()
            if(lexer.currentChar == '#') {
                val start = lexer.bufferPtr
                when(BoostMacroElementImpl.readMacroID(lexer)) {
                    "else" -> {
                        elseEncountered = true
                        ifBody = builder.toString()
                        builder.clear()
                        continue
                    }
                    "endif" -> {
                        if(elseEncountered) elseBody = builder.toString()
                        else ifBody = builder.toString()
                        break
                    }
                    else -> {
                        builder.append('#')
                        lexer.jumpTo(start)
                    }
                }
                lexer.moveForward()
            }

            builder.append(lexer.currentChar)
        }

    }
}