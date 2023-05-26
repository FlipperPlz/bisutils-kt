package com.flipperplz.bisutils.preprocesser.boost.directive

import com.flipperplz.bisutils.parsing.BisLexer
import com.flipperplz.bisutils.preprocesser.boost.utils.BoostDirective
import com.flipperplz.bisutils.preprocesser.boost.utils.DirectiveType

interface BoostIncludeDirective: BoostDirective {
    val stringType: StringType
    val path: String

    override fun getType(): DirectiveType = DirectiveType.B_INCLUDE
    override fun getDirectiveText(): String? = stringType.stringify(path)

    override fun process(arg: Any?): String = processor.locateFile(path) ?: ""

    enum class StringType(val debugName: String, val prefix: Char, val suffix: Char) {
        QUOTED("boost::include.quoted-string", '"', '"'),
        ANGLED("boost::include.angled-string", '<', '>');

        fun stringify(string: String): String = "$prefix$string$suffix"
        fun readRaw(lexer: BisLexer): String = StringBuilder().apply {
            while (length < MAX_STRING_LENGTH && !lexer.isEOF() && lexer.moveForward() != suffix)
                append(lexer.currentChar);
        }.toString()

        companion object {
            const val MAX_STRING_LENGTH = 128
            fun detectType(lexer: BisLexer): StringType? = values().firstOrNull {it.prefix == lexer.currentChar}
        }
    }
}
