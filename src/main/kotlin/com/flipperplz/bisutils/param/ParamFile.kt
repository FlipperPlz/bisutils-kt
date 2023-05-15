package com.flipperplz.bisutils.param

import com.flipperplz.bisutils.param.node.ParamElement
import com.flipperplz.bisutils.param.node.ParamStatementHolder
import com.flipperplz.bisutils.param.utils.ParamElementTypes

interface ParamFile : ParamStatementHolder {
    companion object
    val fileName: String

    override val slimParent: ParamElement?
        get() = null

    override val containingFile: ParamFile?
        get() = null

    override fun getParamElementType(): ParamElementTypes = ParamElementTypes.FILE

    override fun toParam(): String = super.writeSlimCommands()
}