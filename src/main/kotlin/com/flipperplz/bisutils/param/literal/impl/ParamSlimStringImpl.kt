package com.flipperplz.bisutils.param.literal.impl

import com.flipperplz.bisutils.param.literal.RapString
import com.flipperplz.bisutils.param.node.RapElement

data class ParamSlimStringImpl(
    override val parent: RapElement?,
    override var slimValue: String
) : RapString