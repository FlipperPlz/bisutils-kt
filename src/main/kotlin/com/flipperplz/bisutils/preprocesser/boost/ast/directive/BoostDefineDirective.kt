package com.flipperplz.bisutils.preprocesser.boost.ast.directive

import com.flipperplz.bisutils.preprocesser.boost.utils.DirectiveType
import java.lang.StringBuilder

interface BoostDefineDirective : BoostDirective {
    val macroName: String
    val macroValue: String
    val macroArguments: List<String>

    override fun getType(): DirectiveType = DirectiveType.B_DEFINE

    override fun getDirectiveText(): String? = StringBuilder(macroName).apply {
        if(macroArguments.isNotEmpty()) append(macroArguments.joinToString(prefix = "(", postfix = ")") { it })
        if(macroValue.isNotBlank()) append(macroValue)
    }.toString()

    fun evaluate(arguments: List<String>): String? = macroValue.replace(Regex("\\b(${macroArguments.joinToString("|")})\\b")) { matchResult ->
        val argumentIndex = macroArguments.indexOf(matchResult.value)
        arguments.getOrNull(argumentIndex) ?: ""
    }
}