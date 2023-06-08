package com.flipperplz.bisutils.param.ast.literal

import com.flipperplz.bisutils.options.IOptions
import com.flipperplz.bisutils.param.ast.node.IParamLiteral
import com.flipperplz.bisutils.param.utils.ParamStringType

interface IParamString : IParamLiteral<String>, CharSequence {
    val slimStringType: ParamStringType
    override fun isBinarizable(): Boolean = true

    override fun isValid(options: IOptions?): Boolean =
        super.isValid(options) && slimValue.let { if(it == null) false else length <= 1024  }

    override fun toParam(): String =
        slimStringType.stringify(slimValue ?: "")

    companion object {
        const val MAX_STRING_LENGTH: Int = 1024
    }
}