package com.flipperplz.bisutils.param.directive

import com.flipperplz.bisutils.param.node.RapDirective
import com.flipperplz.bisutils.param.node.RapLiteral
import com.flipperplz.bisutils.param.utils.ParamCommandTypes
import com.flipperplz.bisutils.param.utils.ParamElementTypes
import com.flipperplz.bisutils.param.utils.ParamLiteralTypes

//TODO(Ryann): Should also be RapLiteral<RapLiteralBase>
interface RapInclude : RapDirective, RapLiteral<String> {
    val slimIsCommand: Boolean
    override val slimCommandType: ParamCommandTypes
        get() = ParamCommandTypes.PREPROCESSOR_INCLUDE
    override val slimLiteralType: ParamLiteralTypes
        get() = ParamLiteralTypes.PREPROCESSOR_INCLUDE
    override val literalId: Byte?
        get() = null


    //TODO(Ryann): abstract ParamUniversalElement (Literal && Statement)
    override val slimType: ParamElementTypes
        get() = if(slimIsCommand) slimCommandType.type else slimLiteralType.type

    override val slimCurrentlyValid: Boolean
        get() = super<RapDirective>.slimCurrentlyValid && !slimValue.isNullOrBlank()

    override fun toEnforce(): String = "#include <$slimValue>\n"
}