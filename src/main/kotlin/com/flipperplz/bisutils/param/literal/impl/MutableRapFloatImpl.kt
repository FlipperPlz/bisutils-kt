package com.flipperplz.bisutils.param.literal.impl

import com.flipperplz.bisutils.param.literal.RapFloat
import com.flipperplz.bisutils.param.node.RapElement


data class MutableRapFloatImpl(
    override val slimParent: RapElement?,
    override var slimValue: Float
) : RapFloat