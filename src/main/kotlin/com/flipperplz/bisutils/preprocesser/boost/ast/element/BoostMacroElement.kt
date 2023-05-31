package com.flipperplz.bisutils.preprocesser.boost.ast.element

import com.flipperplz.bisutils.preprocesser.boost.BoostPreprocessor
import java.lang.StringBuilder

interface BoostMacroElement : BoostElement {
    val macroName: String
    val macroArguments: List<String>

    override fun toBoost(): String = StringBuilder(macroName).apply {
        append(macroArguments.joinToString(prefix = "(", postfix = ")") { it }).toString()
    }.toString()

    fun process(): String? = process(processor)

    fun process(processor: BoostPreprocessor): String? {
        return processor.locateMacro(macroName)?.evaluate(macroArguments)
    }

}