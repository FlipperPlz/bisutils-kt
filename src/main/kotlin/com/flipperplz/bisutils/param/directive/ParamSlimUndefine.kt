package com.flipperplz.bisutils.param.directive

import com.flipperplz.bisutils.param.node.ParamSlimDirective
import com.flipperplz.bisutils.param.node.RapNamedElement
import com.flipperplz.bisutils.param.utils.ParamCommandTypes

interface ParamSlimUndefine : ParamSlimDirective, RapNamedElement {
    override val slimCommandType: ParamCommandTypes
    get() = ParamCommandTypes.PREPROCESSOR_UNDEFINE
    val macroName: String?

    override val slimCurrentlyValid: Boolean
    get() = super<ParamSlimDirective>.slimCurrentlyValid && !macroName.isNullOrBlank()

    override fun toEnforce(): String = "#undef $macroName\n"
    override val slimName: String?
    get() = macroName
}