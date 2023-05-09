package com.flipperplz.bisutils.param.slim.commands.impl

import com.flipperplz.bisutils.param.slim.ParamSlim
import com.flipperplz.bisutils.param.slim.ParamSlimLiteral
import com.flipperplz.bisutils.param.slim.commands.ParamSlimVariableStatement
import com.flipperplz.bisutils.param.slim.util.ParamOperatorTypes


data class ParamSlimVariableStatementImpl(
    var parentElement: ParamSlim?,
    override var slimName: String?,
    override var slimOperator: ParamOperatorTypes?,
    override var slimValue: ParamSlimLiteral<*>?
) : ParamSlimVariableStatement
