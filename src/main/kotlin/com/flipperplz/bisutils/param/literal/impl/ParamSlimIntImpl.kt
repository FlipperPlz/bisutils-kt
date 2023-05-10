package com.flipperplz.bisutils.param.literal.impl

import com.flipperplz.bisutils.param.literal.RapInt
import com.flipperplz.bisutils.param.node.RapElement

data class ParamSlimIntImpl(
    override val parent: RapElement?,
    override var slimValue: Int
) : RapInt