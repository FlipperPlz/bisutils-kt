package com.flipperplz.bisutils.preprocesser.boost.directive

import com.flipperplz.bisutils.preprocesser.boost.utils.BoostDirective
import com.flipperplz.bisutils.preprocesser.boost.utils.DirectiveType

interface BoostEndIfDirective : BoostDirective {
    override fun getType(): DirectiveType = DirectiveType.B_ENDIF
    override fun getDirectiveText(): String? = null
}