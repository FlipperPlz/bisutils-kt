package com.flipperplz.bisutils.param.rap.statement

import com.flipperplz.bisutils.param.slim.util.ParamOperatorTypes

interface RapFlaggedArrayStatement : RapArrayStatement {
    override val statementId: Byte
        get() = 5

    override val slimOperator: ParamOperatorTypes

    override val slimCurrentlyValid: Boolean
        get() = super<RapArrayStatement>.slimCurrentlyValid && slimOperator != ParamOperatorTypes.ASSIGN
}