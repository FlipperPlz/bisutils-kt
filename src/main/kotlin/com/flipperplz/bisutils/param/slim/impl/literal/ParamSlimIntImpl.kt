package com.flipperplz.bisutils.param.slim.impl.literal

import com.flipperplz.bisutils.param.slim.ParamSlim
import com.flipperplz.bisutils.param.slim.ParamSlimInt


data class ParamSlimIntImpl(
    var parentElement: ParamSlim?,
    override var value: Int
) : ParamSlimInt