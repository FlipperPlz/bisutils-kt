package com.flipperplz.bisutils.param.slim.impl.literal

import com.flipperplz.bisutils.param.slim.ParamSlim
import com.flipperplz.bisutils.param.slim.ParamSlimArray
import com.flipperplz.bisutils.param.slim.ParamSlimLiteral


data class ParamSlimArrayImpl(
    override var parentElement: ParamSlim?,
    override var value: MutableList<ParamSlimLiteral<*>> = mutableListOf()
) : ParamSlimArray