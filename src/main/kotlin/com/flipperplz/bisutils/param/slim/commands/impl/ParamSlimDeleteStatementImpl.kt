package com.flipperplz.bisutils.param.slim.commands.impl

import com.flipperplz.bisutils.param.slim.ParamSlim
import com.flipperplz.bisutils.param.slim.commands.ParamSlimDeleteStatement


data class ParamSlimDeleteStatementImpl(
    var parentElement: ParamSlim?,
    override var slimDeleteTarget: String?
) : ParamSlimDeleteStatement