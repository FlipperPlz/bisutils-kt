package com.flipperplz.bisutils.param.utils.mutability

import com.flipperplz.bisutils.param.ParamFile
import com.flipperplz.bisutils.param.ast.node.ParamElement
import com.flipperplz.bisutils.param.ast.node.ParamStatement
import com.flipperplz.bisutils.param.utils.mutability.node.ParamMutableStatementHolder

class ParamMutableFileImpl(
    override var fileName: String,
    override var slimCommands: MutableList<ParamStatement> = mutableListOf(),
): ParamMutableFile {
    override var slimParent: ParamElement? = null
    override var containingParamFile: ParamFile? = null
}

interface ParamMutableFile : ParamMutableStatementHolder, ParamFile {
    override val fileName: String
    override var slimCommands: MutableList<ParamStatement>
    override var containingParamFile: ParamFile?
    override var slimParent: ParamElement?
}