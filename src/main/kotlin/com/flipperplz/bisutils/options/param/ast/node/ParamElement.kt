package com.flipperplz.bisutils.options.param.ast.node

import com.flipperplz.bisutils.options.param.ParamFile
import com.flipperplz.bisutils.options.param.utils.ParamElementTypes

interface ParamElement {
    val slimParent: ParamElement?
    val containingParamFile: ParamFile?
    fun getParamElementType(): ParamElementTypes
    fun isCurrentlyValid(): Boolean
    fun isBinarizable(): Boolean
    fun toParam(): String

    companion object {
        val REGEX_ALPHANUM: Regex = Regex("^[a-zA-Z0-9]+$")
    }
}