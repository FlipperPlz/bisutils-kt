package com.flipperplz.bisutils.param.slim.impl.literal

import com.flipperplz.bisutils.param.slim.ParamSlim
import com.flipperplz.bisutils.param.slim.ParamSlimFloat


data class ParamSlimFloatImpl(
    override var parentElement: ParamSlim?,
    override var value: Float
) : ParamSlimFloat