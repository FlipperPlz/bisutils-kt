package com.flipperplz.bisutils.preprocesser.boost.utils


typealias DirectiveType = BoostDirectiveType
interface BoostDirective {
    fun getType(): DirectiveType
    fun toBoost(): String
    fun toBoost(includeSharp: Boolean, includeNewLine: Boolean): String {
        val builder = StringBuilder()
        if(includeSharp) builder.append('#')
        builder.append(toBoost())
        if(includeNewLine) builder.append("\n")
        return builder.toString()
    }
}