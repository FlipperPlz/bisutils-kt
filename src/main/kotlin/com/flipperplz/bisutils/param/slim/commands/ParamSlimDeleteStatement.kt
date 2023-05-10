package com.flipperplz.bisutils.param.slim.commands

import com.flipperplz.bisutils.param.slim.node.ParamSlimCommand
import com.flipperplz.bisutils.param.slim.node.ParamSlimNamed
import com.flipperplz.bisutils.param.slim.util.ParamCommandTypes

interface ParamSlimDeleteStatement : ParamSlimCommand, ParamSlimNamed {
    val slimDeleteTarget: String?

    override val slimCommandType: ParamCommandTypes
        get() = ParamCommandTypes.DELETE
    override val slimCurrentlyValid: Boolean
        get() =  super<ParamSlimCommand>.slimCurrentlyValid && !slimDeleteTarget.isNullOrBlank()

    override val slimName: String?
        get() = slimDeleteTarget

    override fun toEnforce(): String = "delete $slimDeleteTarget;"
}