package com.flipperplz.bisutils.param.slim.structure.commands

import com.flipperplz.bisutils.param.slim.structure.ParamSlimCommand


interface ParamSlimExternalClass : ParamSlimCommand {
    val slimClassName: String?
    override fun toEnforce(): String = "class $slimClassName;"
    override fun currentlyValid(): Boolean = !slimClassName.isNullOrEmpty() && super.currentlyValid()
}