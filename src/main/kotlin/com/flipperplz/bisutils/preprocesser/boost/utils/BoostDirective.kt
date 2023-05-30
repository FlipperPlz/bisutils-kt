package com.flipperplz.bisutils.preprocesser.boost.utils

import com.flipperplz.bisutils.parsing.BisLexer
import com.flipperplz.bisutils.preprocesser.boost.BoostPreprocessor


typealias DirectiveType = BoostDirectiveType
interface BoostDirective {
    val processor: BoostPreprocessor
    fun getType(): DirectiveType
    fun getDirectiveText(): String?
    fun toBoost(includeSharp: Boolean, includeNewLine: Boolean): String {
        val builder = StringBuilder()
        if(includeSharp) builder.append('#')
        builder.append(getType().text)
        getDirectiveText()?.let { builder.append(' ').append(it) }
        if(includeNewLine) builder.append("\n")
        return builder.toString()
    }
}