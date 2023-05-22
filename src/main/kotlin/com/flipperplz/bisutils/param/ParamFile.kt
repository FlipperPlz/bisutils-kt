package com.flipperplz.bisutils.param

import com.flipperplz.bisutils.param.ast.node.ParamElement
import com.flipperplz.bisutils.param.ast.node.ParamStatementHolder
import com.flipperplz.bisutils.param.utils.ParamElementTypes

interface ParamFile : ParamStatementHolder {
    companion object
    val fileName: String

    override val slimParent: ParamElement?
        get() = null

    override val containingParamFile: ParamFile?
        get() = null

    override fun getParamElementType(): ParamElementTypes = ParamElementTypes.FILE

    override fun toParam(): String = super.writeSlimCommands()
}