package com.flipperplz.bisutils.param.statement.impl

import com.flipperplz.bisutils.param.node.RapElement
import com.flipperplz.bisutils.param.statement.RapDeleteStatement


data class MutableRapDeleteStatementImpl(
    override val slimParent: RapElement?,
    override var slimDeleteTarget: String
) : RapDeleteStatement