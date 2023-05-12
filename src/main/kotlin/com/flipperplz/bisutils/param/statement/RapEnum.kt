package com.flipperplz.bisutils.param.statement

import com.flipperplz.bisutils.param.node.RapElement.Companion.REGEX_ALPHANUM
import com.flipperplz.bisutils.param.node.RapStatement
import com.flipperplz.bisutils.param.utils.ParamElementTypes
import java.lang.StringBuilder

interface RapEnum: RapStatement {
    val enumValues: Map<String, Int>?

    override fun isBinarizable(): Boolean = true

    override fun isCurrentlyValid(): Boolean =
        enumValues?.keys?.all { REGEX_ALPHANUM.matches(it) } ?: true

    override fun getRapElementType(): ParamElementTypes = ParamElementTypes.C_ENUM

    override fun toParam(): String = if(enumValues.isNullOrEmpty())
        "" else
        StringBuilder("enum {\n").append(
            enumValues!!.entries.joinToString(separator = ",\n") { it.key + "=" + it.value }
        ).append("};").toString()
}