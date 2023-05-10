package com.flipperplz.bisutils.param.statement.impl

import com.flipperplz.bisutils.param.node.RapElement
import com.flipperplz.bisutils.param.statement.RapDeleteStatement


data class ParamSlimDeleteStatementImpl(
    override val parent: RapElement?,
    override var slimDeleteTarget: String
) : RapDeleteStatement