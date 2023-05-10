package com.flipperplz.bisutils.param.slim

import com.flipperplz.bisutils.param.slim.node.ParamSlimCommand
import com.flipperplz.bisutils.param.slim.util.ParamElementTypes

data class ParamSlimFileImpl(
    override var fileName: String,
    override var slimEnum: MutableMap<String, Int> = mutableMapOf(),
    override var slimCommands: MutableList<ParamSlimCommand> = mutableListOf()
) : ParamSlimFile