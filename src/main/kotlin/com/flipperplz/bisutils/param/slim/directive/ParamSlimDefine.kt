package com.flipperplz.bisutils.param.slim.directive

import com.flipperplz.bisutils.param.slim.node.ParamSlimDirective
import com.flipperplz.bisutils.param.slim.util.ParamCommandTypes

interface ParamSlimDefine : ParamSlimDirective {
    override val slimCommandType: ParamCommandTypes
        get() = ParamCommandTypes.PREPROCESSOR_INCLUDE
    val slimMacroName: String?
    val slimMacroArguments: List<String>?
    val slimMacroValue: String?

    fun evaluateMacro(vararg argument: String): String? {
        if(slimMacroArguments.isNullOrEmpty()) return slimMacroValue;
        var currentString = slimMacroValue
        slimMacroArguments?.let {
            argument.withIndex().forEach { (i, arg) ->
                currentString = currentString?.replace(it[i].toRegex(), arg)
            }
        }

        return currentString.toString()
    }

    override val slimCurrentlyValid: Boolean
        get() = super.slimCurrentlyValid && !slimMacroName.isNullOrBlank()

    override fun toEnforce(): String {
        val builder = StringBuilder("#define $slimMacroName")
        if(!slimMacroArguments.isNullOrEmpty()) builder.append(slimMacroArguments!!.joinToString(
            prefix = "(",
            separator = ", ",
            postfix = ")"
        ) { it })
        return builder.append(" ").append(slimMacroValue?.replace("\n", "\\\n")).toString()
    }
}