package com.flipperplz.bisutils.param.utils.mutability.node

import com.flipperplz.bisutils.param.ast.node.IParamStatement
import com.flipperplz.bisutils.param.ast.node.IParamStatementHolder

interface ParamMutableStatementHolder: ParamMutableElement, IParamStatementHolder {
    override var slimCommands: MutableList<IParamStatement>
}