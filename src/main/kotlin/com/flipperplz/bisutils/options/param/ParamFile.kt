package com.flipperplz.bisutils.options.param

import com.flipperplz.bisutils.options.param.ast.node.ParamElement
import com.flipperplz.bisutils.options.param.ast.node.ParamNamedElement
import com.flipperplz.bisutils.options.param.ast.node.ParamStatementHolder
import com.flipperplz.bisutils.options.param.utils.ParamElementTypes

interface ParamFile : ParamStatementHolder, ParamNamedElement {
    companion object;
    override val slimParent: ParamElement?
        get() = null

    override val containingParamFile: ParamFile?
        get() = null

    override fun getParamElementType(): ParamElementTypes = ParamElementTypes.FILE

    override fun toParam(): String = super.writeSlimCommands()
}