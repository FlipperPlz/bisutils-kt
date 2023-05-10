package com.flipperplz.bisutils.param.directive

import com.flipperplz.bisutils.param.node.ParamSlimDirective
import com.flipperplz.bisutils.param.utils.ParamCommandTypes

//TODO(Ryann): Should also be RapLiteral<RapLiteralBase>
interface ParamSlimInclude : ParamSlimDirective {
    override val slimCommandType: ParamCommandTypes
        get() = ParamCommandTypes.PREPROCESSOR_INCLUDE
    val path: String?

    override val slimCurrentlyValid: Boolean
        get() = super.slimCurrentlyValid && !path.isNullOrBlank()

    override fun toEnforce(): String = "#include <$path>\n"
}