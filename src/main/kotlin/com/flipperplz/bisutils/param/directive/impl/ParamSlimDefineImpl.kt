package com.flipperplz.bisutils.param.directive.impl

import com.flipperplz.bisutils.param.directive.ParamSlimDefine
import com.flipperplz.bisutils.param.node.RapElement

data class ParamSlimDefineImpl(
    override val parent: RapElement?,
    override var slimMacroName: String?,
    override var slimMacroArguments: List<String>,
    override var slimMacroValue: String?
) : ParamSlimDefine