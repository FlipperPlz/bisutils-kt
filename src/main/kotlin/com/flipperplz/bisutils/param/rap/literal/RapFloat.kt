package com.flipperplz.bisutils.param.rap.literal

import com.flipperplz.bisutils.param.rap.node.RapLiteral
import com.flipperplz.bisutils.param.slim.literals.ParamSlimFloat

interface RapFloat : ParamSlimFloat, RapLiteral {
    override val literalId: Byte
        get() = 1
}