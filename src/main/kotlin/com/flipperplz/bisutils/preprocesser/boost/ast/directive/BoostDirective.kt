package com.flipperplz.bisutils.preprocesser.boost.ast.directive

import com.flipperplz.bisutils.preprocesser.boost.BoostPreprocessor
import com.flipperplz.bisutils.preprocesser.boost.ast.element.BoostElement
import com.flipperplz.bisutils.preprocesser.boost.utils.DirectiveType

interface BoostDirective : BoostElement {
    override val processor: BoostPreprocessor
    fun getType(): DirectiveType
    fun getDirectiveText(): String?

    override fun toBoost(): String = toBoost(includeSharp = true, includeNewLine = true)
    fun toBoost(includeSharp: Boolean, includeNewLine: Boolean): String {
        val builder = StringBuilder()
        if(includeSharp) builder.append('#')
        builder.append(getType().text)
        getDirectiveText()?.let { builder.append(' ').append(it) }
        if(includeNewLine) builder.append("\n")
        return builder.toString()
    }
}