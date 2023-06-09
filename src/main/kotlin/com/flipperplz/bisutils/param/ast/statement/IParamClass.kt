package com.flipperplz.bisutils.param.ast.statement

import com.flipperplz.bisutils.options.IOptions
import com.flipperplz.bisutils.param.ast.node.IParamElement
import com.flipperplz.bisutils.param.ast.node.IParamStatementHolder

interface IParamClass : IParamElement, IParamExternalClass, IParamStatementHolder {
    companion object
    val paramSuperClassname: String?

    override fun isValid(options: IOptions?): Boolean = super<IParamExternalClass>.isValid(options)

    override fun toParam(): String {
        val builder = StringBuilder(super<IParamExternalClass>.toParam().trimEnd(';'))
        if (!paramSuperClassname.isNullOrBlank()) builder.append(" : ").append(paramSuperClassname)
        builder.append(" { \n")
        builder.append(super<IParamStatementHolder>.toParam())
        return builder.append("};").toString()
    }
}