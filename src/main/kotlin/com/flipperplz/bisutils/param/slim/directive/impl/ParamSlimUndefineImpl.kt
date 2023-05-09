package com.flipperplz.bisutils.param.slim.directive.impl

import com.flipperplz.bisutils.param.slim.directive.ParamSlimUndefine
import com.flipperplz.bisutils.param.slim.node.ParamSlim

data class ParamSlimUndefineImpl(
    var parentElement: ParamSlim?,
    override var macroName: String,
): ParamSlimUndefine