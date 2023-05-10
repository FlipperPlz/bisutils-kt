package com.flipperplz.bisutils.param.rap.literal

import com.flipperplz.bisutils.param.rap.node.RapLiteral
import com.flipperplz.bisutils.param.slim.literals.ParamSlimArray

interface RapArray : ParamSlimArray, RapLiteral {
    override val literalId: Byte
        get() = 3

    override val slimValue: List<RapLiteral>?
}