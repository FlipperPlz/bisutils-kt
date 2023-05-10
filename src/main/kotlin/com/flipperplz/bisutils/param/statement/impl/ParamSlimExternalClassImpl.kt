package com.flipperplz.bisutils.param.statement.impl

import com.flipperplz.bisutils.param.node.RapElement
import com.flipperplz.bisutils.param.statement.RapExternalClass


data class ParamSlimExternalClassImpl(
    override val parent: RapElement?,
    override var slimClassName: String
) : RapExternalClass