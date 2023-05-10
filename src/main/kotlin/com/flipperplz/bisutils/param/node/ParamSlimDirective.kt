package com.flipperplz.bisutils.param.node

interface ParamSlimDirective : RapStatement {
    override val slimBinarizable: Boolean
        get() = false

    override val statementId: Byte
        get() = -1
}