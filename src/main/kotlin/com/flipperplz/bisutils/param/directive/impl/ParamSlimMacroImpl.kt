package com.flipperplz.bisutils.param.directive.impl

import com.flipperplz.bisutils.param.directive.ParamSlimMacro
import com.flipperplz.bisutils.param.node.RapElement

data class ParamSlimMacroImpl(
    override val parent: RapElement?,
    override var slimIsCommand: Boolean,
    override var slimMacroName: String,
    override var slimMacroArguments: List<String>,
): ParamSlimMacro
