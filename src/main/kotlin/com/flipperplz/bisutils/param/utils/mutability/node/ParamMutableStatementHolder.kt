package com.flipperplz.bisutils.param.utils.mutability.node

import com.flipperplz.bisutils.param.node.ParamElement
import com.flipperplz.bisutils.param.node.ParamStatement
import com.flipperplz.bisutils.param.node.ParamStatementHolder

interface ParamMutableStatementHolder: ParamMutableElement, ParamStatementHolder {
    override var slimCommands: MutableList<ParamStatement>
}