package com.flipperplz.bisutils.param.slim.directive

import com.flipperplz.bisutils.param.slim.node.ParamSlimDirective
import com.flipperplz.bisutils.param.slim.util.ParamCommandTypes

interface ParamSlimInclude : ParamSlimDirective {
    override val slimCommandType: ParamCommandTypes
        get() = ParamCommandTypes.PREPROCESSOR_INCLUDE
    val path: String?

    override val slimCurrentlyValid: Boolean
        get() = super.slimCurrentlyValid && !path.isNullOrBlank()

    override fun toEnforce(): String = "#include <$path>\n"
}