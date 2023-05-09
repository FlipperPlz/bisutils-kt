package com.flipperplz.bisutils.param.slim.commands

import com.flipperplz.bisutils.param.slim.ParamSlimCommand
import com.flipperplz.bisutils.param.slim.util.ParamCommandTypes

interface ParamSlimDeleteStatement : ParamSlimCommand {
    val slimDeleteTarget: String?

    override val slimCommandType: ParamCommandTypes
        get() = ParamCommandTypes.DELETE
    override val slimCurrentlyValid: Boolean
        get() =  super.slimCurrentlyValid && !slimDeleteTarget.isNullOrBlank()

    override fun toEnforce(): String = "delete $slimDeleteTarget;"
}