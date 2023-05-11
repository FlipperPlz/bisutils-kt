package com.flipperplz.bisutils.param.directive.impl

import com.flipperplz.bisutils.param.directive.RapUndefine
import com.flipperplz.bisutils.param.node.RapElement

data class RapUndefineImpl(
    override val slimParent: RapElement?,
    override var macroName: String,
) : RapUndefine