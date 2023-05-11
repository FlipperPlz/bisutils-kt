package com.flipperplz.bisutils.param.directive

import com.flipperplz.bisutils.param.node.RapDirective
import com.flipperplz.bisutils.param.node.RapElement
import com.flipperplz.bisutils.param.node.RapNamedElement
import com.flipperplz.bisutils.param.utils.ParamElementTypes

interface RapDefine : RapDirective, RapNamedElement {
    val slimMacroArguments: List<String>?
    val slimMacroValue: String?

    fun evaluateMacro(arguments: List<String>): List<RapElement>

    override fun isCurrentlyValid(): Boolean =
        !slimName.isNullOrBlank()

    override fun getRapElementType(): ParamElementTypes = ParamElementTypes.CP_DEFINE

    override fun toParam(): String {
        val builder = StringBuilder("#define $slimName")
        if (!slimMacroArguments.isNullOrEmpty()) builder.append(slimMacroArguments!!.joinToString(
            prefix = "(",
            separator = ", ",
            postfix = ")"
        ) { it })
        return builder.append(" ").append(slimMacroValue?.replace("\n", "\\\n")).append('\n').toString()
    }
}