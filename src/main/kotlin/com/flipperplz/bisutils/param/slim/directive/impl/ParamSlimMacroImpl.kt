package com.flipperplz.bisutils.param.slim.directive.impl

import com.flipperplz.bisutils.param.slim.directive.ParamSlimMacro
import com.flipperplz.bisutils.param.slim.node.ParamSlim

data class ParamSlimMacroImpl(
    var parentElement: ParamSlim?,
    override var slimIsCommand: Boolean,
    override var slimMacroName: String,
    override var slimMacroArguments: List<String>,
): ParamSlimMacro
