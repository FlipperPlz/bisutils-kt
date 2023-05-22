package com.flipperplz.bisutils.param.utils.mutability.node

import com.flipperplz.bisutils.param.ast.node.ParamStatement
import com.flipperplz.bisutils.param.ast.node.ParamStatementHolder

interface ParamMutableStatementHolder: ParamMutableElement, ParamStatementHolder {
    override var slimCommands: MutableList<ParamStatement>
}