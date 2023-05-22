package com.flipperplz.bisutils.param.ast.statement

import com.flipperplz.bisutils.param.ast.node.ParamElement
import com.flipperplz.bisutils.param.ast.node.ParamStatementHolder
import com.flipperplz.bisutils.param.utils.ParamElementTypes

interface ParamClass : ParamElement, ParamExternalClass, ParamStatementHolder {
    companion object
    val slimSuperClass: String?

    override fun isBinarizable(): Boolean = true
    override fun getParamElementType(): ParamElementTypes = ParamElementTypes.C_CLASS

    fun locateSuperClass(): ParamExternalClass?

    fun shouldValidateSuper(): Boolean

    override fun isCurrentlyValid(): Boolean = if(slimSuperClass.isNullOrBlank() || !shouldValidateSuper())
        super<ParamExternalClass>.isCurrentlyValid() else
        locateSuperClass() != null

    override fun toParam(): String {
        val builder = StringBuilder(super<ParamExternalClass>.toParam().trimEnd(';'))
        if (!slimSuperClass.isNullOrBlank()) builder.append(" : ").append(slimSuperClass)
        builder.append(" { \n")
        builder.append(writeSlimCommands())
        return builder.append("};").toString()
    }
}