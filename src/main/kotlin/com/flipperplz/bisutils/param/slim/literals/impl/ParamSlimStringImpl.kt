package com.flipperplz.bisutils.param.slim.literals.impl

import com.flipperplz.bisutils.param.slim.ParamSlim
import com.flipperplz.bisutils.param.slim.literals.ParamSlimString


data class ParamSlimStringImpl(
    var parentElement: ParamSlim?,
    override var slimValue: String
) : ParamSlimString