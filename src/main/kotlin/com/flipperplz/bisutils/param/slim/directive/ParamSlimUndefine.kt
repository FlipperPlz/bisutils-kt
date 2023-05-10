package com.flipperplz.bisutils.param.slim.directive

import com.flipperplz.bisutils.param.slim.node.ParamSlimDirective
import com.flipperplz.bisutils.param.slim.node.ParamSlimNamed
import com.flipperplz.bisutils.param.slim.util.ParamCommandTypes

interface ParamSlimUndefine : ParamSlimDirective, ParamSlimNamed {
    override val slimCommandType: ParamCommandTypes
        get() = ParamCommandTypes.PREPROCESSOR_UNDEFINE
    val macroName: String?

    override val slimCurrentlyValid: Boolean
        get() = super<ParamSlimDirective>.slimCurrentlyValid && !macroName.isNullOrBlank()

    override fun toEnforce(): String = "#undef $macroName\n"
    override val slimName: String?
        get() = macroName
}