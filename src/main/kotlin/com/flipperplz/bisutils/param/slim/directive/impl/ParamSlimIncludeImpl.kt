package com.flipperplz.bisutils.param.slim.directive.impl

import com.flipperplz.bisutils.param.slim.directive.ParamSlimDefine
import com.flipperplz.bisutils.param.slim.directive.ParamSlimInclude
import com.flipperplz.bisutils.param.slim.node.ParamSlim

data class ParamSlimIncludeImpl(
    var parentElement: ParamSlim?,
    override var path: String?
) : ParamSlimInclude