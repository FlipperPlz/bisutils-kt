package com.flipperplz.bisutils.param.rap.statement

import com.flipperplz.bisutils.param.rap.node.RapStatement
import com.flipperplz.bisutils.param.slim.commands.ParamSlimDeleteStatement

interface RapDeleteStatement : ParamSlimDeleteStatement, RapStatement {
    override val statementId: Byte
        get() = 4
}