package com.flipperplz.bisutils.param.rap.statement

import com.flipperplz.bisutils.param.rap.node.RapStatement
import com.flipperplz.bisutils.param.slim.commands.ParamSlimExternalClass

interface RapExternalClass : ParamSlimExternalClass, RapStatement {
    override val statementId: Byte
        get() = 3
}