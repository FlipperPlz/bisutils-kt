package com.flipperplz.bisutils.param.rap.node

import com.flipperplz.bisutils.param.slim.node.ParamSlimCommand

interface RapStatement : ParamSlimCommand, RapElement {
    val statementId: Byte
}