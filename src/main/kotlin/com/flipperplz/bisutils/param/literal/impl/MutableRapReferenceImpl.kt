package com.flipperplz.bisutils.param.literal.impl

import com.flipperplz.bisutils.param.literal.RapReference
import com.flipperplz.bisutils.param.node.RapElement

data class MutableRapReferenceImpl(
    override val slimParent: RapElement?,
    override var slimValue: String?
) : RapReference