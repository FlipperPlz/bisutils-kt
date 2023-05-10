package com.flipperplz.bisutils.param.statement.impl

import com.flipperplz.bisutils.param.node.RapElement
import com.flipperplz.bisutils.param.node.RapLiteralBase
import com.flipperplz.bisutils.param.statement.RapVariableStatement
import com.flipperplz.bisutils.param.utils.ParamOperatorTypes

data class ParamSlimVariableStatementImpl(
    override val parent: RapElement?,
    override var slimName: String,
    override var slimOperator: ParamOperatorTypes = ParamOperatorTypes.ASSIGN,
) : RapVariableStatement {
    override lateinit var slimValue: RapLiteralBase

}
