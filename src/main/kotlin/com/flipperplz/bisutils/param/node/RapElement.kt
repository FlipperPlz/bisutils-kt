package com.flipperplz.bisutils.param.node

import com.flipperplz.bisutils.param.RapFile
import com.flipperplz.bisutils.param.utils.ParamElementTypes

interface RapElement {
    val slimParent: RapElement?
    val containingFile: RapFile?
    fun getRapElementType(): ParamElementTypes
    fun isCurrentlyValid(): Boolean
    fun isBinarizable(): Boolean
    fun toParam(): String

    companion object {
        val REGEX_ALPHANUM: Regex = Regex("^[a-zA-Z0-9]+$")
    }
}