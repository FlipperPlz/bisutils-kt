package com.flipperplz.bisutils.param.ast.node

import com.flipperplz.bisutils.options.IOptions

interface IParamLiteralBase : IParamElement {
    companion object

    val slimValue: Any?

    override fun isValid(options: IOptions?): Boolean = slimValue != null
}