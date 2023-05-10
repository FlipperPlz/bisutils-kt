package com.flipperplz.bisutils.param.statement.impl

import com.flipperplz.bisutils.param.node.RapElement
import com.flipperplz.bisutils.param.node.RapStatement
import com.flipperplz.bisutils.param.statement.RapClass


data class MutableRapClassImpl(
    override val parent: RapElement?,
    override var slimSuperClass: String?,
    override var slimCommands: List<RapStatement>,
    override var slimClassName: String,
) : RapClass