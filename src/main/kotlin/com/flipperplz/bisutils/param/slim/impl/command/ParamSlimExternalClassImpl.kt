package com.flipperplz.bisutils.param.slim.impl.command

import com.flipperplz.bisutils.param.slim.ParamSlim
import com.flipperplz.bisutils.param.slim.ParamSlimExternalClass


data class ParamSlimExternalClassImpl(
    override var parentElement: ParamSlim?,
    override var className: String
) : ParamSlimExternalClass