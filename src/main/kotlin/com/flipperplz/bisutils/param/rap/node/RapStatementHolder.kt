package com.flipperplz.bisutils.param.rap.node

import com.flipperplz.bisutils.param.slim.node.ParamSlimCommandHolder

interface RapStatementHolder : ParamSlimCommandHolder, RapElement {
    val binaryOffset: Int
    override val slimCommands: List<RapStatement>
}