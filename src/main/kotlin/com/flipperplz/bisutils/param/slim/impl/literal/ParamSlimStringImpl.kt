package com.flipperplz.bisutils.param.slim.impl.literal

import com.flipperplz.bisutils.param.slim.ParamSlim
import com.flipperplz.bisutils.param.slim.ParamSlimString


data class ParamSlimStringImpl(
    override var parentElement: ParamSlim?,
    override var value: String
) : ParamSlimString