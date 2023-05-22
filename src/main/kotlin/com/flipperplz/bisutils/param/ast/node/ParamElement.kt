package com.flipperplz.bisutils.param.ast.node

import com.flipperplz.bisutils.param.ParamFile
import com.flipperplz.bisutils.param.utils.ParamElementTypes

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