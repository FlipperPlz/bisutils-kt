package com.flipperplz.bisutils.param.slim.commands.impl

import com.flipperplz.bisutils.param.slim.ParamSlim
import com.flipperplz.bisutils.param.slim.commands.ParamSlimExternalClass


data class ParamSlimExternalClassImpl(
    var parentElement: ParamSlim?,
    override var slimClassName: String?
) : ParamSlimExternalClass