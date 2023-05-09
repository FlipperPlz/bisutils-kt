package com.flipperplz.bisutils.param.slim.literals.impl

import com.flipperplz.bisutils.param.slim.node.ParamSlim
import com.flipperplz.bisutils.param.slim.literals.ParamSlimFloat


data class ParamSlimFloatImpl(
    var parentElement: ParamSlim?,
    override var slimValue: Float
) : ParamSlimFloat