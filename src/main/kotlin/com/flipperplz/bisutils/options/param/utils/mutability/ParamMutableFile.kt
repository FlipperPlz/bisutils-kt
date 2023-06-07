package com.flipperplz.bisutils.options.param.utils.mutability

import com.flipperplz.bisutils.options.param.ParamFile
import com.flipperplz.bisutils.options.param.ast.node.ParamElement
import com.flipperplz.bisutils.options.param.ast.node.ParamStatement
import com.flipperplz.bisutils.options.param.utils.mutability.node.ParamMutableStatementHolder
import com.flipperplz.bisutils.utils.IFlushable

class ParamMutableFileImpl(
    override var slimName: String?,
    override var slimCommands: MutableList<ParamStatement> = mutableListOf(),
): ParamMutableFile {
    override var slimParent: ParamElement? = null
    override var containingParamFile: ParamFile? = null
}

interface ParamMutableFile : ParamMutableStatementHolder, ParamFile, IFlushable {
    override var slimName: String?
    override var slimCommands: MutableList<ParamStatement>
    override var containingParamFile: ParamFile?
    override var slimParent: ParamElement?

    override fun flush() {
        slimCommands.clear()
    }
}