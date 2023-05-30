package com.flipperplz.bisutils.preprocesser.boost.directive

import com.flipperplz.bisutils.preprocesser.boost.utils.BoostDirective
import com.flipperplz.bisutils.preprocesser.boost.utils.BoostDirectiveType
import com.flipperplz.bisutils.preprocesser.boost.utils.DirectiveType

interface BoostIfDefinedDirective : BoostDirective {
    val macroName: String
    override fun getType(): DirectiveType = BoostDirectiveType.B_IFDEF

    val ifBody: String
    val elseBody: String?

    fun evaluate(): String = if (processor.locateMacro(macroName) == null) ifBody else elseBody ?: ""

    override fun getDirectiveText(): String? = StringBuilder(macroName).apply {
        append('\n')
        append(ifBody)
        elseBody?.let {
            append('\n')
            append("#else\n")
            append(it)
        }
        append("\n#endif")
    }.toString()

}