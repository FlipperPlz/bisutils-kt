package com.flipperplz.bisutils.param.slim.node

import com.flipperplz.bisutils.param.slim.util.ParamElementTypes
import com.flipperplz.bisutils.param.slim.util.ParamLiteralTypes

interface ParamSlimLiteral<T> : ParamSlimLiteralBase {
    override val slimValue: T?
}
