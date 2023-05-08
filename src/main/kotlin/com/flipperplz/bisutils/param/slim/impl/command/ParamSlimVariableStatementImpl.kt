package com.flipperplz.bisutils.param.slim.impl.command

import com.flipperplz.bisutils.param.slim.ParamSlim
import com.flipperplz.bisutils.param.slim.ParamSlimLiteral
import com.flipperplz.bisutils.param.slim.ParamSlimVariableStatement
import com.flipperplz.bisutils.param.slim.util.ParamOperatorTypes


data class ParamSlimVariableStatementImpl(
    var parentElement: ParamSlim?,
    override var operator: ParamOperatorTypes,
    override var name: String,
    override var value: ParamSlimLiteral<*>
) : ParamSlimVariableStatement
