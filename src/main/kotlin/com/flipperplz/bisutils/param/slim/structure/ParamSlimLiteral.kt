package com.flipperplz.bisutils.param.slim.structure

import com.flipperplz.bisutils.param.slim.ParamSlim
import com.flipperplz.bisutils.param.slim.util.ParamLiteralTypes

interface ParamSlimLiteral<T> : ParamSlim {
    val literalType: ParamLiteralTypes
    val value: T?

    override fun currentlyValid(): Boolean = value != null

}