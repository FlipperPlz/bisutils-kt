package com.flipperplz.bisutils.param.statement.impl

import com.flipperplz.bisutils.param.node.RapElement
import com.flipperplz.bisutils.param.node.RapLiteralBase
import com.flipperplz.bisutils.param.statement.RapVariableStatement
import com.flipperplz.bisutils.param.utils.ParamOperatorTypes

data class MutableRapVariableStatementImpl(
    override val slimParent: RapElement?,
    override var slimName: String,
    override var slimOperator: ParamOperatorTypes = ParamOperatorTypes.ASSIGN,
) : RapVariableStatement {
    override lateinit var slimValue: RapLiteralBase

}
