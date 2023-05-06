package com.flipperplz.bisutils.param.slim.impl.command

import com.flipperplz.bisutils.param.slim.ParamOperator
import com.flipperplz.bisutils.param.slim.ParamSlim
import com.flipperplz.bisutils.param.slim.ParamSlimLiteral
import com.flipperplz.bisutils.param.slim.ParamSlimVariableStatement


data class ParamSlimVariableStatementImpl(
    override var parentElement: ParamSlim?,
    override var operator: ParamOperator,
    override var name: String,
    override var value: ParamSlimLiteral<*>
) : ParamSlimVariableStatement
