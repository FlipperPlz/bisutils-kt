package com.flipperplz.bisutils.preprocesser.boost.directive

import com.flipperplz.bisutils.preprocesser.boost.utils.BoostDirective
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

}