package com.flipperplz.bisutils.param.slim.literals.impl

import com.flipperplz.bisutils.param.slim.node.ParamSlim
import com.flipperplz.bisutils.param.slim.node.ParamSlimLiteral
import com.flipperplz.bisutils.param.slim.literals.ParamSlimArray


data class ParamSlimArrayImpl(
    var parentElement: ParamSlim?,
    override val slimValue: MutableList<ParamSlimLiteral<*>>?
) : ParamSlimArray