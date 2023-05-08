package com.flipperplz.bisutils.param.slim.impl.command

import com.flipperplz.bisutils.param.slim.ParamSlim
import com.flipperplz.bisutils.param.slim.ParamSlimDeleteStatement


data class ParamSlimDeleteStatementImpl(
    var parentElement: ParamSlim?,
    override var target: String
) : ParamSlimDeleteStatement