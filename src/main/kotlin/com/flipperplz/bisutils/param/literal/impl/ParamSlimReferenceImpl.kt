package com.flipperplz.bisutils.param.literal.impl

import com.flipperplz.bisutils.param.literal.ParamSlimReference
import com.flipperplz.bisutils.param.node.RapElement

data class ParamSlimReferenceImpl(
    override val parent: RapElement?,
    override var slimValue: String?
) : ParamSlimReference