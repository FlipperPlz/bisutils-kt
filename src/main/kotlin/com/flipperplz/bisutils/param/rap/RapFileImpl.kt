package com.flipperplz.bisutils.param.rap

import com.flipperplz.bisutils.param.rap.node.RapStatement
import com.flipperplz.bisutils.param.slim.node.ParamSlimCommand


//TODO(Ryann): Change all to List and make lateinit. Rap should have mininal mutability
class RapFileImpl(
    override val fileName: String,
    override val slimEnum: MutableMap<String, Int> = mutableMapOf(),
    override val slimCommands: MutableList<RapStatement> = mutableListOf()
) : RapFile