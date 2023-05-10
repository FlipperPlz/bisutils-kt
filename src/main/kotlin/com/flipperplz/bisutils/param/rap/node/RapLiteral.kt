package com.flipperplz.bisutils.param.rap.node

import com.flipperplz.bisutils.param.slim.node.ParamSlimLiteralBase

interface RapLiteral : RapElement, ParamSlimLiteralBase {
    val literalId: Byte
}