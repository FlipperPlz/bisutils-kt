package com.flipperplz.bisutils.param.literal.impl

import com.flipperplz.bisutils.param.literal.RapFloat
import com.flipperplz.bisutils.param.node.RapElement


data class ParamSlimFloatImpl(
    override val parent: RapElement?,
    override var slimValue: Float
) : RapFloat