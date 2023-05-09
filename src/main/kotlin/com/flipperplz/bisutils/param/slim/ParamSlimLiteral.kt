package com.flipperplz.bisutils.param.slim

import com.flipperplz.bisutils.param.slim.util.ParamElementTypes
import com.flipperplz.bisutils.param.slim.util.ParamLiteralTypes

interface ParamSlimLiteral<T> : ParamSlim {
    val slimLiteralType: ParamLiteralTypes
    override val slimType: ParamElementTypes
        get() = slimLiteralType.type

    val slimValue: T?

    override val slimCurrentlyValid: Boolean
        get() = slimValue != null
}