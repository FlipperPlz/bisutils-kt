package com.flipperplz.bisutils.param.slim.literals.impl

import com.flipperplz.bisutils.param.slim.node.ParamSlim
import com.flipperplz.bisutils.param.slim.literals.ParamSlimInt


data class ParamSlimIntImpl(
    var parentElement: ParamSlim?,
    override var slimValue: Int
) : ParamSlimInt