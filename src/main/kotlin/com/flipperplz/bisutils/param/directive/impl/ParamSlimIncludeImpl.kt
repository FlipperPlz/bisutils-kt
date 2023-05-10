package com.flipperplz.bisutils.param.directive.impl

import com.flipperplz.bisutils.param.directive.ParamSlimInclude
import com.flipperplz.bisutils.param.node.RapElement


data class ParamSlimIncludeImpl(
    override val parent: RapElement?,
    override val slimValue: String?,
    override val slimIsCommand: Boolean
) : ParamSlimInclude