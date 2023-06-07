package com.flipperplz.bisutils.options.param.utils.mutability.node

import com.flipperplz.bisutils.options.param.ast.node.ParamStatement
import com.flipperplz.bisutils.options.param.ast.node.ParamStatementHolder

interface ParamMutableStatementHolder: ParamMutableElement, ParamStatementHolder {
    override var slimCommands: MutableList<ParamStatement>
}