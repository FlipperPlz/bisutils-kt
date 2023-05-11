package com.flipperplz.bisutils.param.statement.impl

import com.flipperplz.bisutils.param.node.RapElement
import com.flipperplz.bisutils.param.statement.RapExternalClass


data class MutableRapExternalClassImpl(
    override val slimParent: RapElement?,
    override var slimClassName: String
) : RapExternalClass