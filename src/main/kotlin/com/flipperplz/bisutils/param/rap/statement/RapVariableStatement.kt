package com.flipperplz.bisutils.param.rap.statement

import com.flipperplz.bisutils.param.rap.node.RapStatement
import com.flipperplz.bisutils.param.slim.commands.ParamSlimVariableStatement
import com.flipperplz.bisutils.param.slim.literals.ParamSlimArray
import com.flipperplz.bisutils.param.slim.util.ParamOperatorTypes

interface RapVariableStatement : ParamSlimVariableStatement, RapStatement {
    override val slimOperator: ParamOperatorTypes
        get() = ParamOperatorTypes.ASSIGN

    override val statementId: Byte
        get() = 1

    override val slimCurrentlyValid: Boolean
        get() = super<ParamSlimVariableStatement>.slimCurrentlyValid && slimValue !is ParamSlimArray

}