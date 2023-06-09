package com.flipperplz.bisutils.param.utils.mutability

import com.flipperplz.bisutils.param.ast.IParamFile
import com.flipperplz.bisutils.param.ast.node.IParamElement
import com.flipperplz.bisutils.param.ast.node.IParamStatement
import com.flipperplz.bisutils.param.utils.mutability.node.ParamMutableStatementHolder
import com.flipperplz.bisutils.utils.IFlushable

class ParamMutableFileImpl(
    override var slimName: String?,
    override var slimCommands: MutableList<IParamStatement> = mutableListOf(),
): ParamMutableFile {
    override var slimParent: IParamElement? = null
    override var containingParamFile: IParamFile? = null
}

interface ParamMutableFile : ParamMutableStatementHolder, IParamFile, IFlushable {
    override var slimName: String?
    override var slimCommands: MutableList<IParamStatement>
    override var containingParamFile: IParamFile?
    override var slimParent: IParamElement?

    override fun flush() {
        slimCommands.clear()
    }
}