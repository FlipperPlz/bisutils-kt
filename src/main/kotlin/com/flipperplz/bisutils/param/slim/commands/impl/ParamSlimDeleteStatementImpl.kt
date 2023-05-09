package com.flipperplz.bisutils.param.slim.commands.impl

import com.flipperplz.bisutils.param.slim.node.ParamSlim
import com.flipperplz.bisutils.param.slim.commands.ParamSlimDeleteStatement


data class ParamSlimDeleteStatementImpl(
    var parentElement: ParamSlim?,
    override var slimDeleteTarget: String
) : ParamSlimDeleteStatement