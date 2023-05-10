package com.flipperplz.bisutils.param.directive.impl

import com.flipperplz.bisutils.param.directive.ParamSlimUndefine
import com.flipperplz.bisutils.param.node.RapElement

data class ParamSlimUndefineImpl(
    override val parent: RapElement?,
    override var macroName: String,
): ParamSlimUndefine