package com.flipperplz.bisutils.param.directive.impl

import com.flipperplz.bisutils.param.directive.RapInclude
import com.flipperplz.bisutils.param.node.RapElement

data class RapIncludeImpl(
    override val parent: RapElement?,
    override val slimValue: String?,
    override val slimIsCommand: Boolean
) : RapInclude