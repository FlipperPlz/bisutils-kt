package com.flipperplz.bisutils.param.node

import com.flipperplz.bisutils.param.utils.ParamElementTypes
import com.flipperplz.bisutils.param.utils.ParamLiteralTypes

interface RapLiteralBase : RapElement {
    val slimValue: Any?
    val literalId: Byte?

    val slimLiteralType: ParamLiteralTypes
    override val slimType: ParamElementTypes
        get() = slimLiteralType.type

    override val slimCurrentlyValid: Boolean
        get() = slimValue != null
}