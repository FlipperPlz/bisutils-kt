package com.flipperplz.bisutils.param.rap.literal

import com.flipperplz.bisutils.param.rap.node.RapLiteral
import com.flipperplz.bisutils.param.slim.literals.ParamSlimString

interface RapString : ParamSlimString, RapLiteral {
    override val literalId: Byte
        get() = 0
}