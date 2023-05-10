package com.flipperplz.bisutils.param.slim.commands

import com.flipperplz.bisutils.param.slim.node.ParamSlimCommand
import com.flipperplz.bisutils.param.slim.node.ParamSlimNamed
import com.flipperplz.bisutils.param.slim.util.ParamCommandTypes


interface ParamSlimExternalClass : ParamSlimCommand, ParamSlimNamed {
    val slimClassName: String?
    override val slimCommandType: ParamCommandTypes
        get() = ParamCommandTypes.EXTERNAL_CLASS
    override val slimCurrentlyValid: Boolean
        get() = super<ParamSlimCommand>.slimCurrentlyValid && !slimClassName.isNullOrBlank()
    override fun toEnforce(): String = "class $slimClassName;"
    override val slimName: String?
        get() = slimClassName
}