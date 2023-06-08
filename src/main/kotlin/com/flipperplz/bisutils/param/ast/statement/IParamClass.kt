package com.flipperplz.bisutils.param.ast.statement

import com.flipperplz.bisutils.options.IOptions
import com.flipperplz.bisutils.param.ast.node.IParamElement
import com.flipperplz.bisutils.param.ast.node.IParamStatementHolder
import com.flipperplz.bisutils.param.utils.ParamElementTypes

interface IParamClass : IParamElement, IParamExternalClass, IParamStatementHolder {
    companion object
    val slimSuperClass: String?

    override fun isBinarizable(): Boolean = true

    fun locateSuperClass(): IParamExternalClass?

    fun shouldValidateSuper(): Boolean
    override fun isValid(options: IOptions?): Boolean {
        return if(slimSuperClass.isNullOrBlank() || !shouldValidateSuper())
            super<IParamExternalClass>.isValid(options)
        else locateSuperClass() != null
    }

    override fun toParam(): String {
        val builder = StringBuilder(super<IParamExternalClass>.toParam().trimEnd(';'))
        if (!slimSuperClass.isNullOrBlank()) builder.append(" : ").append(slimSuperClass)
        builder.append(" { \n")
        builder.append(super<IParamStatementHolder>.toParam())
        return builder.append("};").toString()
    }
}