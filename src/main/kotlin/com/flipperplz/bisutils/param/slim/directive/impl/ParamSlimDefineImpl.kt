package com.flipperplz.bisutils.param.slim.directive.impl

import com.flipperplz.bisutils.param.slim.directive.ParamSlimDefine
import com.flipperplz.bisutils.param.slim.node.ParamSlim

data class ParamSlimDefineImpl(
    var parentElement: ParamSlim?,
    override var slimMacroName: String?,
    override var slimMacroArguments: List<String>,
    override var slimMacroValue: String?
) : ParamSlimDefine