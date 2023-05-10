package com.flipperplz.bisutils.param.rap.literal

import com.flipperplz.bisutils.param.rap.node.RapLiteral
import com.flipperplz.bisutils.param.slim.literals.ParamSlimInt

interface RapInt : ParamSlimInt, RapLiteral {
    override val literalId: Byte
        get() = 2
}