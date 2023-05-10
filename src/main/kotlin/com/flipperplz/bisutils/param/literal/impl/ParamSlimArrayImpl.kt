package com.flipperplz.bisutils.param.literal.impl

import com.flipperplz.bisutils.param.literal.RapArray
import com.flipperplz.bisutils.param.node.RapElement
import com.flipperplz.bisutils.param.node.RapLiteralBase


data class ParamSlimArrayImpl(
    override val parent: RapElement?,
    override var slimValue: MutableList<RapLiteralBase>
) : RapArray