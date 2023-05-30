package com.flipperplz.bisutils.preprocesser.boost.directive

import com.flipperplz.bisutils.preprocesser.boost.utils.BoostDirective
import com.flipperplz.bisutils.preprocesser.boost.utils.DirectiveType

interface BoostUndefineDirective : BoostDirective {
    val macroName: String
    override fun getType(): DirectiveType = DirectiveType.B_UNDEFINE
    override fun getDirectiveText(): String? = macroName
}