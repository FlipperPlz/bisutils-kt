package com.flipperplz.bisutils.param.literal.impl

import com.flipperplz.bisutils.param.literal.RapString
import com.flipperplz.bisutils.param.node.RapElement

data class MutableRapStringImpl(
    override val slimParent: RapElement?,
    override var slimValue: String
) : RapString