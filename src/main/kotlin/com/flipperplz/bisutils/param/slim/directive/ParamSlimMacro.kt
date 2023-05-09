package com.flipperplz.bisutils.param.slim.directive

import com.flipperplz.bisutils.param.slim.node.ParamSlimDirective
import com.flipperplz.bisutils.param.slim.node.ParamSlimLiteral
import com.flipperplz.bisutils.param.slim.util.ParamCommandTypes
import com.flipperplz.bisutils.param.slim.util.ParamElementTypes
import com.flipperplz.bisutils.param.slim.util.ParamLiteralTypes

interface ParamSlimMacro : ParamSlimDirective, ParamSlimLiteral<ParamSlimMacro> {
    override val slimCommandType: ParamCommandTypes
        get() = ParamCommandTypes.MACRO

    override val slimLiteralType: ParamLiteralTypes
        get() = ParamLiteralTypes.MACRO

    override val slimType: ParamElementTypes
        get() = if(slimIsCommand) ParamElementTypes.C_MACRO else ParamElementTypes.L_MACRO

    override val slimValue: ParamSlimMacro
        get() = this

    val slimIsCommand: Boolean
    val slimMacroName: String?
    val slimMacroArguments: List<String>?

    override val slimCurrentlyValid: Boolean
        get() = super<ParamSlimLiteral>.slimCurrentlyValid && slimMacroName.isNullOrBlank()

    override fun toEnforce(): String = slimMacroName + slimMacroArguments?.joinToString(
        prefix = "(",
        separator = ",",
        postfix = if(slimIsCommand)
            ");" else
            ")"
    ) {
        it
    }

}