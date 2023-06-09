package com.flipperplz.bisutils.param.ast.statement

import com.flipperplz.bisutils.options.IOptions
import com.flipperplz.bisutils.param.ast.node.IParamStatement

interface IParamDeleteStatement : IParamStatement {
    companion object;
    val paramTarget: String?

    override fun isValid(options: IOptions?): Boolean = !paramTarget.isNullOrBlank()

    override fun toParam(): String = "delete $paramTarget;"
}