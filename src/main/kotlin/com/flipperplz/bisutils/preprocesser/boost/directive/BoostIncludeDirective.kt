package com.flipperplz.bisutils.preprocesser.boost.directive

import com.flipperplz.bisutils.preprocesser.boost.utils.BoostDirective
import com.flipperplz.bisutils.preprocesser.boost.utils.DirectiveType

interface BoostIncludeDirective: BoostDirective {
    val stringType: StringType
    val path: String

    override fun getType(): DirectiveType = DirectiveType.B_INCLUDE
    override fun getDirectiveText(): String? = stringType.stringify(path)


    enum class StringType(val debugName: String, val prefix: Char, val suffix: Char) {
        QUOTED("boost::include.quoted-string", '"', '"'),
        ANGLED("boost::include.angled-string", '<', '>');

        fun stringify(string: String): String = "$prefix$string$suffix"
    }
}
