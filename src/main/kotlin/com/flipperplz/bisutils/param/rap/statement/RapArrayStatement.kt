package com.flipperplz.bisutils.param.rap.statement

import com.flipperplz.bisutils.param.rap.literal.RapArray
import com.flipperplz.bisutils.param.rap.node.RapStatement
import com.flipperplz.bisutils.param.slim.commands.ParamSlimVariableStatement
import com.flipperplz.bisutils.param.slim.util.ParamOperatorTypes

interface RapArrayStatement : ParamSlimVariableStatement, RapStatement {
    override val slimValue: RapArray
    override val slimOperator: ParamOperatorTypes
        get() = ParamOperatorTypes.ASSIGN

    override val statementId: Byte
        get() = 2

}