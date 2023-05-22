package com.flipperplz.bisutils.param.utils.mutability

import com.flipperplz.bisutils.param.ParamFile
import com.flipperplz.bisutils.param.ast.node.ParamElement
import com.flipperplz.bisutils.param.ast.node.ParamStatement
import com.flipperplz.bisutils.param.utils.mutability.node.ParamMutableStatementHolder

class ParamMutableFile(
    override var fileName: String,
    override var slimCommands: MutableList<ParamStatement> = mutableListOf(),
): ParamMutableStatementHolder, ParamFile {
    override var slimParent: ParamElement? = null
    override var containingParamFile: ParamFile? = null
}