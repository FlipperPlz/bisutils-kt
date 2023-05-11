package com.flipperplz.bisutils.param.directive.impl

import com.flipperplz.bisutils.param.directive.RapDefine
import com.flipperplz.bisutils.param.node.RapElement

data class RapDefineImpl(
    override val slimParent: RapElement?,
    override var slimMacroName: String?,
    override var slimMacroArguments: List<String>,
    override var slimMacroValue: String?
) : RapDefine