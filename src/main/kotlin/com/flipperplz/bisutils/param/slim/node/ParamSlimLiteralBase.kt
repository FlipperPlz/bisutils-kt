package com.flipperplz.bisutils.param.slim.node

import com.flipperplz.bisutils.param.slim.util.ParamElementTypes
import com.flipperplz.bisutils.param.slim.util.ParamLiteralTypes

interface ParamSlimLiteralBase : ParamSlim {
    val slimValue: Any?

    val slimLiteralType: ParamLiteralTypes
    override val slimType: ParamElementTypes
        get() = slimLiteralType.type

    override val slimCurrentlyValid: Boolean
        get() = slimValue != null
}