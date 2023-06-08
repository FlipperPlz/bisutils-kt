package com.flipperplz.bisutils.param.ast.statement

import com.flipperplz.bisutils.options.IOptions
import com.flipperplz.bisutils.param.ast.node.IParamElement.Companion.REGEX_ALPHANUM
import com.flipperplz.bisutils.param.ast.node.IParamNumerical
import com.flipperplz.bisutils.param.ast.node.IParamStatement
import java.lang.StringBuilder

interface IParamEnum: IParamStatement {
    val enumValues: Map<String, IParamNumerical>?

    override fun isBinarizable(): Boolean = true

    override fun isValid(options: IOptions?): Boolean =
        enumValues?.keys?.all { REGEX_ALPHANUM.matches(it) } ?: true

    override fun toParam(): String = if(enumValues.isNullOrEmpty())
        "" else
        StringBuilder("enum {\n").append(
            enumValues!!.entries.joinToString(separator = ",\n") { it.key + "=" + it.value }
        ).append("};").toString()
}