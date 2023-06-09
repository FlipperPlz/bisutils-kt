package com.flipperplz.bisutils.param.ast.statement

import com.flipperplz.bisutils.options.IOptions
import com.flipperplz.bisutils.param.ast.node.IParamElement
import com.flipperplz.bisutils.param.ast.node.IParamStatement

interface IParamExternalClass : IParamStatement {
    companion object;
    val paramClassname: String?

    override fun isValid(options: IOptions?): Boolean =
            paramClassname != null &&
            IParamElement.REGEX_ALPHANUM.matches(paramClassname!!)

    override fun toParam(): String = "class $paramClassname;"
}