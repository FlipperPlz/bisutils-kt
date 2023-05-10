package com.flipperplz.bisutils.param.directive

import com.flipperplz.bisutils.param.node.ParamSlimDirective
import com.flipperplz.bisutils.param.node.RapLiteral
import com.flipperplz.bisutils.param.node.RapNamedElement
import com.flipperplz.bisutils.param.utils.ParamCommandTypes
import com.flipperplz.bisutils.param.utils.ParamElementTypes
import com.flipperplz.bisutils.param.utils.ParamLiteralTypes

interface ParamSlimMacro : ParamSlimDirective, RapLiteral<ParamSlimMacro>, RapNamedElement {
    override val slimCommandType: ParamCommandTypes
        get() = ParamCommandTypes.MACRO

    override val slimLiteralType: ParamLiteralTypes
        get() = ParamLiteralTypes.PREPROCESSOR_MACRO

    override val literalId: Byte?
        get() = null

    override val slimType: ParamElementTypes
        get() = if(slimIsCommand) ParamElementTypes.C_MACRO else ParamElementTypes.L_MACRO

    override val slimValue: ParamSlimMacro
        get() = this

    val slimIsCommand: Boolean
    val slimMacroName: String?
    val slimMacroArguments: List<String>?

    override val slimName: String?
        get() = slimMacroName

    override val slimCurrentlyValid: Boolean
        get() = super<RapLiteral>.slimCurrentlyValid && slimMacroName.isNullOrBlank()

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