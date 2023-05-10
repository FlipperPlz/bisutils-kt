package com.flipperplz.bisutils.param.slim.commands

import com.flipperplz.bisutils.param.slim.node.ParamSlimCommandHolder
import com.flipperplz.bisutils.param.slim.util.ParamCommandTypes

interface ParamSlimClass : ParamSlimExternalClass, ParamSlimCommandHolder {
    val slimSuperClass: String?
    override val slimCommandType: ParamCommandTypes
        get() = ParamCommandTypes.CLASS

    override val slimCurrentlyValid: Boolean
        get() = super<ParamSlimExternalClass>.slimCurrentlyValid &&
                super<ParamSlimCommandHolder>.slimCurrentlyValid &&
                (slimSuperClass?.isNotEmpty() ?: true)


    override fun toEnforce(): String {
        val builder = StringBuilder(super<ParamSlimExternalClass>.toEnforce())
        if (slimSuperClass != null) builder.append(" : ").append(slimSuperClass)
        builder.append(" { \n")
        builder.append(super<ParamSlimCommandHolder>.toEnforce())
        return builder.append("};").toString()
    }
}