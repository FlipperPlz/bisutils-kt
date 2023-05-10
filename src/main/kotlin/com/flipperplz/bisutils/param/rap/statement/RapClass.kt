package com.flipperplz.bisutils.param.rap.statement

import com.flipperplz.bisutils.param.rap.node.RapElement
import com.flipperplz.bisutils.param.rap.node.RapStatement
import com.flipperplz.bisutils.param.rap.node.RapStatementHolder
import com.flipperplz.bisutils.param.slim.commands.ParamSlimClass

interface RapClass : ParamSlimClass, RapElement, RapExternalClass, RapStatementHolder {
    override val statementId: Byte
        get() = 0

}