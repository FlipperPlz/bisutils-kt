package com.flipperplz.bisutils.param.slim.structure.commands

import com.flipperplz.bisutils.param.slim.structure.ParamSlimCommand

interface ParamSlimDeleteStatement : ParamSlimCommand {
    val slimDeleteTarget: String?

    override fun currentlyValid(): Boolean = super.currentlyValid() && !slimDeleteTarget.isNullOrBlank()

    override fun toEnforce(): String = "delete $slimDeleteTarget;"
}