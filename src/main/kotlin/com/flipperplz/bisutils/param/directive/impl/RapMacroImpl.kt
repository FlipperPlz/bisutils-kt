package com.flipperplz.bisutils.param.directive.impl

import com.flipperplz.bisutils.param.directive.RapMacro
import com.flipperplz.bisutils.param.node.RapElement

data class RapMacroImpl(
    override val parent: RapElement?,
    override var slimIsCommand: Boolean,
    override var slimMacroName: String,
    override var slimMacroArguments: List<String>,
) : RapMacro
