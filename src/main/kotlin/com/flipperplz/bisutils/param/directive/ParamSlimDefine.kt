package com.flipperplz.bisutils.param.directive

import com.flipperplz.bisutils.param.node.ParamSlimDirective
import com.flipperplz.bisutils.param.node.RapNamedElement
import com.flipperplz.bisutils.param.utils.ParamCommandTypes

interface ParamSlimDefine : ParamSlimDirective, RapNamedElement {
    override val slimCommandType: ParamCommandTypes
        get() = ParamCommandTypes.PREPROCESSOR_DEFINE
    val slimMacroName: String?
    val slimMacroArguments: List<String>?
    val slimMacroValue: String?

    override val slimName: String?
        get() = slimMacroName

    fun evaluateMacro(vararg argument: String): String? {
        if (slimMacroArguments.isNullOrEmpty()) return slimMacroValue
        var currentString = slimMacroValue
        slimMacroArguments?.let {
            argument.withIndex().forEach { (i, arg) ->
                currentString = currentString?.replace(it[i].toRegex(), arg)
            }
        }

        return currentString.toString()
    }

    override val slimCurrentlyValid: Boolean
        get() = super<ParamSlimDirective>.slimCurrentlyValid && !slimMacroName.isNullOrBlank()

    override fun toEnforce(): String {
        val builder = StringBuilder("#define $slimMacroName")
        if (!slimMacroArguments.isNullOrEmpty()) builder.append(slimMacroArguments!!.joinToString(
            prefix = "(",
            separator = ", ",
            postfix = ")"
        ) { it })
        return builder.append(" ").append(slimMacroValue?.replace("\n", "\\\n")).append('\n').toString()
    }
}