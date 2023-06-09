package com.flipperplz.bisutils.param.ast.literal

import com.flipperplz.bisutils.options.IOptions
import com.flipperplz.bisutils.param.ast.node.IParamLiteral
import com.flipperplz.bisutils.param.utils.ParamStringType

interface IParamString : IParamLiteral, CharSequence {
    val slimStringType: ParamStringType?
    override val paramValue: String?

    override fun isValid(options: IOptions?): Boolean =
        super.isValid(options) && paramValue.let { if(it == null) false else length <= 1024  }

    override fun toParam(): String =
        slimStringType!!.stringify(paramValue ?: "")

    companion object {
        const val MAX_STRING_LENGTH: Int = 1024
    }
}