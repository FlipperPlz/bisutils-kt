package com.flipperplz.bisutils.param.ast.statement

import com.flipperplz.bisutils.param.ast.node.ParamElement.Companion.REGEX_ALPHANUM
import com.flipperplz.bisutils.param.ast.node.ParamStatement
import com.flipperplz.bisutils.param.utils.ParamElementTypes
import java.lang.StringBuilder

interface ParamEnum: ParamStatement {
    val enumValues: Map<String, Int>?

    override fun isBinarizable(): Boolean = true

    override fun isCurrentlyValid(): Boolean =
        enumValues?.keys?.all { REGEX_ALPHANUM.matches(it) } ?: true

    override fun getParamElementType(): ParamElementTypes = ParamElementTypes.C_ENUM

    override fun toParam(): String = if(enumValues.isNullOrEmpty())
        "" else
        StringBuilder("enum {\n").append(
            enumValues!!.entries.joinToString(separator = ",\n") { it.key + "=" + it.value }
        ).append("};").toString()
}