package com.flipperplz.bisutils.param.directive

import com.flipperplz.bisutils.param.node.RapDirective
import com.flipperplz.bisutils.param.node.RapNamedElement
import com.flipperplz.bisutils.param.utils.ParamCommandTypes

interface RapUndefine : RapDirective, RapNamedElement {
    override val slimCommandType: ParamCommandTypes
        get() = ParamCommandTypes.PREPROCESSOR_UNDEFINE
    val macroName: String?

    override val slimCurrentlyValid: Boolean
        get() = super<RapDirective>.slimCurrentlyValid && !macroName.isNullOrBlank()

    override fun toParam(): String = "#undef $macroName\n"
    override val slimName: String?
        get() = macroName
}