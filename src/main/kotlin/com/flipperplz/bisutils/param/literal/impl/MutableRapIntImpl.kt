package com.flipperplz.bisutils.param.literal.impl

import com.flipperplz.bisutils.param.literal.RapInt
import com.flipperplz.bisutils.param.node.RapElement

data class MutableRapIntImpl(
    override val slimParent: RapElement?,
    override var slimValue: Int
) : RapInt